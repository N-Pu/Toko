import java.io.File

fun main() {
// Ввод пути к папке с классами
    println("Введите путь к папке с классами:")
    val folderPath = readLine()?.trim() ?: return
    // Ввод количества классов, которые нужно обработать
    println("Введите количество классов для обработки:")
    val classCount = readLine()?.toIntOrNull() ?: return

// Поиск файлов с расширением .kt в указанной папке
    val kotlinFiles = File(folderPath).walkTopDown().maxDepth(1)
        .filter { it.isFile && it.extension == "kt" }.toList()

// Проверка, что найдено достаточное количество файлов
    if (kotlinFiles.size < classCount) {
        println("Ошибка: найдено меньше классов, чем указано для обработки")
        return
    }

// Обработка каждого файла
    val classNames = mutableListOf<String>()
    for (i in 0 until classCount) {
        val kotlinFile = kotlinFiles[i]
        // Чтение содержимого файла
        val fileContent = kotlinFile.readText()

        // Поиск строки с объявлением пакета
        val packageRegex = Regex("^\\s*package\\s+.+;\\s*$", RegexOption.MULTILINE)
        val packageMatch = packageRegex.find(fileContent)

        // Определение позиции, куда нужно вставить импорт
        val importPosition = packageMatch?.range?.last?.plus(1) ?: 0

        // Регулярное выражение для поиска переменных в классе
        val regex = Regex("(val|var)\\s+(\\w+):\\s+(\\w+)")

        // Поиск переменных в классе
        val classMatches = regex.findAll(fileContent)
        // Извлечение имени класса из имени файла
        val className = kotlinFile.nameWithoutExtension
        classNames.add(className)

        // Обновление содержимого файла
        var updatedFileContent = fileContent
        classMatches.forEach { classMatch ->
            // Извлечение содержимого переменной
            val classContent = classMatch.groups[0]?.value
            if (classContent != null) {
                // Генерация аннотаций @SerializedName перед каждой переменной
                val updatedClassContent =
                    """@SerializedName("${classMatch.groups[2]?.value}") $classContent"""
                // Замена содержимого переменной на обновленное
                updatedFileContent = updatedFileContent.replace(classContent, updatedClassContent)
            }
        }

        // Вставка импорта
        val importStatement = "import com.google.gson.annotations.SerializedName\n"
        updatedFileContent = updatedFileContent.substring(
            0,
            importPosition
        ) + importStatement + updatedFileContent.substring(importPosition)

        // Запись обновленного содержимого файла
        kotlinFile.writeText(updatedFileContent)
    }

// Вывод обработанных классов
    println("Обработаны классы: $classNames")
}