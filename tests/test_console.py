"""Pruebas de regresion reales sobre el ejecutable Java, con datos temporales."""
import shutil
import subprocess
import tempfile
import unittest
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


class ConsoleTests(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.build = tempfile.TemporaryDirectory(prefix="verpelis-build-")
        cls.classes = Path(cls.build.name) / "classes"
        sources = [str(p) for p in (ROOT / "src").rglob("*.java")]
        subprocess.run(["javac", "--release", "21", "-encoding", "UTF-8",
                        "-d", str(cls.classes), *sources], check=True)

    @classmethod
    def tearDownClass(cls):
        cls.build.cleanup()

    def setUp(self):
        self.temp = tempfile.TemporaryDirectory(prefix="verpelis-data-")
        self.directory = Path(self.temp.name)
        (self.directory / "src").mkdir()
        self.data = self.directory / "src/contenido.txt"
        shutil.copyfile(ROOT / "src/contenido.txt", self.data)

    def tearDown(self):
        self.temp.cleanup()

    def execute(self, inputs, *, success=True, data=None):
        command = ["java"]
        if data is not None:
            command.append("-Dverpelis.data=" + str(data))
        command += ["-cp", str(self.classes), "com.platzi.proyecto.MainClass"]
        result = subprocess.run(command, input=inputs, text=True, capture_output=True,
                                cwd=self.directory, timeout=10)
        if success:
            self.assertEqual(result.returncode, 0, result.stderr)
        else:
            self.assertNotEqual(result.returncode, 0)
        self.assertNotIn('Exception in thread', result.stderr)
        return result.stdout + result.stderr

    def test_catalog_search_sort_and_play(self):
        out = self.execute("2\n4\nshrek\n3\nanimada\n5\n2\nsi\n6\nShrek\n10\n")
        for text in ["Numero de elementos en la plataforma: 15", "Pelicula encontrada con exito",
                     "5 peliculas encontradas", "Peliculas ordenadas con exito", "Reproduciendo Shrek"]:
            self.assertIn(text, out)

    def test_add_and_delete_survive_restart(self):
        self.execute("1\n1\nPrueba nueva\n90\naccion\n12\n2026-01-01\n10\n")
        self.assertIn("Pelicula encontrada con exito", self.execute("4\nPrueba nueva\n10\n"))
        self.execute("9\nPrueba nueva\n10\n")
        self.assertIn("Pelicula no encontrada", self.execute("4\nPrueba nueva\n10\n"))
        self.assertNotIn("Prueba nueva", self.data.read_text())

    def test_documentary_round_trip(self):
        self.execute("1\n2\nDocumental nuevo\n42\nciencia_ficcion\n0\n2026-01-01\nNarrador nuevo\n10\n")
        self.assertIn("Narrador nuevo", self.execute("4\nDocumental nuevo\n10\n"))
        self.assertIn("DOCUMENTAL|Documental nuevo|42|CIENCIA_FICCION|0|2026-01-01|Narrador nuevo", self.data.read_text())

    def test_invalid_inputs_retry_without_crashing(self):
        out = self.execute("1\n99\n1\nPrueba valida\n-1\n90\ninventado\nciencia ficcion\n-3\n12\n2026-02-30\n2026-01-01\n5\n-1\n0\n2\nno\n10\n")
        self.assertIn("Felicidades", out)
        self.assertIn("genero no encontrado", out)
        self.assertIn("fecha valida", out)
        self.assertIn("Peliculas ordenadas con exito", out)

    def test_duplicate_title_does_not_change_file(self):
        before = self.data.read_bytes()
        out = self.execute("1\n1\n shrek \n90\nanimada\n12\n2026-01-01\n10\n")
        self.assertIn("ya existe", out)
        self.assertEqual(self.data.read_bytes(), before)

    def test_export_and_unsafe_filename(self):
        self.execute("7\nprueba\n10\n")
        export = self.directory / "exports/prueba.txt"
        self.assertEqual(export.read_bytes(), self.data.read_bytes())
        self.assertIn("Error:", self.execute("7\n../contenido\n10\n"))

    def test_eof_exits_cleanly_even_mid_prompt(self):
        self.assertIn("Gracias por usar", self.execute(""))
        self.assertIn("Gracias por usar", self.execute("1\n"))

    def test_corrupt_data_fails_without_overwriting(self):
        self.data.write_text("PELICULA|Invalida|90|ACCION|12|fecha-invalida\n")
        before = self.data.read_bytes()
        self.assertIn("linea 1", self.execute("10\n", success=False))
        self.assertEqual(self.data.read_bytes(), before)

    def test_unreadable_catalog_fails_without_stacktrace(self):
        self.data.unlink()
        self.data.mkdir()
        out = self.execute("10\n", success=False)
        self.assertIn("No se pudo leer", out)

    def test_failed_save_does_not_change_in_memory_catalog(self):
        process = subprocess.Popen(["java", "-cp", str(self.classes),
                                    "com.platzi.proyecto.MainClass"], stdin=subprocess.PIPE,
                                   stdout=subprocess.PIPE, stderr=subprocess.PIPE,
                                   text=True, cwd=self.directory)
        try:
            while True:
                line = process.stdout.readline()
                self.assertTrue(line, "El programa termino antes del menu")
                if "Ingresa la opcion que quieres hacer:" in line:
                    break
            self.data.unlink()
            self.data.mkdir()
            (self.data / "bloqueo").touch()
            out, err = process.communicate("1\n1\nNo debe guardarse\n90\naccion\n12\n2026-01-01\n2\n10\n", timeout=10)
            self.assertEqual(process.returncode, 0, err)
            self.assertIn("No se pudo guardar", out)
            self.assertNotIn("Felicidades", out)
            self.assertIn("Numero de elementos en la plataforma: 15", out)
        finally:
            if process.poll() is None:
                process.kill()
                process.communicate()

    def test_new_data_file_can_be_created(self):
        data = self.directory / "nuevo/catalogo.txt"
        self.execute("1\n1\nNueva\n90\naccion\n0\n2026-01-01\n10\n", data=data)
        self.assertTrue(data.is_file())
        self.assertIn("Pelicula encontrada con exito", self.execute("4\nNueva\n10\n", data=data))


if __name__ == "__main__":
    unittest.main(verbosity=2)
