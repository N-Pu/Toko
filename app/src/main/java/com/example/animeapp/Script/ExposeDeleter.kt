import java.io.File

fun main() {
    val folderPath = "E:\\Stuff\\AnimeApp\\app\\src\\main\\java\\com\\example\\animeapp\\domain\\searchModel" // Укажите путь до папки, в которой нужно найти файлы (классы)
    findAndProcessFiles(folderPath)
}

fun findAndProcessFiles(folderPath: String) {
    val folder = File(folderPath)
    if (folder.exists() && folder.isDirectory) {
        val files = folder.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isDirectory) {
                    // Если это папка, рекурсивно вызываем функцию для поиска внутри подпапки
                    findAndProcessFiles(file.absolutePath)
                } else {
                    // Если это файл, обрабатываем его
                    if (file.name.endsWith(".kt")) {
                        processFile(file)
                    }
                }
            }
        }
    } else {
        println("Указанная папка не существует или не является директорией.")
    }
}

fun processFile(file: File) {
    println("Обработка файла: ${file.absolutePath}")

    // Читаем содержимое файла
    val lines = file.readLines().toMutableList()

    // Флаг, указывающий на изменения в файле
    var fileChanged = false

    // Итерируем по строкам файла
    for (i in lines.indices) {
        var line = lines[i]

        // Удаляем аннотацию @Expose у переменных
        if (line.contains("@Expose")) {
            line = line.replace("@Expose", "")
            fileChanged = true
        }

        // Записываем измененную строку обратно в файл
        lines[i] = line
    }

    // Если были внесены изменения, сохраняем файл
    if (fileChanged) {
        file.writeText(lines.joinToString("\n"))
        println("Файл успешно обработан и изменен: ${file.absolutePath}")
    } else {
        println("Файл не содержит аннотацию @Expose: ${file.absolutePath}")
    }
}
