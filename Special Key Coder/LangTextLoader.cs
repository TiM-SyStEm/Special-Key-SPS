using System.IO;
using System.Collections.Generic;
using System.Windows.Forms;
using Inmutef.GoogleTranslate.Translate;


namespace Special_Key_Coder
{
    class LangTextLoader
    {
        public static BaseText Start()
        {
            string lang = File.ReadAllText("SaveLangIs.txt");
            return main_loader(lang);
        }

        protected static BaseText main_loader(string lang)
        {
             string folc = "Файл";
             string fileopen = "Открыть файл";
             string save = "Сохранить";
             string saveas = "Сохранить как";
             string runc = "Запустить";
             string runconsole = "Запустить консоль";
             string comp ="Запустить компиляцию";
             string docu ="Документация";
             string oi = "Отчёт";
             string vid = "Вид";
             string exa = "Примеры";
             string no = "Нет информации об пути";
             string oshhib = "Ошибка подцветки синтаксиса";
             string savefileforcomp = "Сначала сохраните файл!";
             string filenet = "Файл не найден";
             string language = "Язык";

             
            string file = new GoogleTranslate().TranslateText(folc, "ru", lang);
            string fileOpen = new GoogleTranslate().TranslateText(fileopen, "ru", lang);
            string fileSave = new GoogleTranslate().TranslateText(save, "ru", lang);
            string fileSaveAs = new GoogleTranslate().TranslateText(saveas, "ru", lang);
            string run = new GoogleTranslate().TranslateText(runc, "ru", lang);
            string runConsole = new GoogleTranslate().TranslateText(runconsole, "ru", lang);
            string runCompiling = new GoogleTranslate().TranslateText(comp, "ru", lang);
            string docs = new GoogleTranslate().TranslateText(docu, "ru", lang);
            string report = new GoogleTranslate().TranslateText(oi, "ru", lang);
            string view = new GoogleTranslate().TranslateText(vid, "ru", lang);
            string examples = new GoogleTranslate().TranslateText(exa, "ru", lang);
            string noInfo = new GoogleTranslate().TranslateText(no, "ru", lang);
            string err = new GoogleTranslate().TranslateText(oshhib, "ru", lang);
            string saveFileForComp = new GoogleTranslate().TranslateText(savefileforcomp, "ru", lang);
            string fileNoFound = new GoogleTranslate().TranslateText(filenet, "ru", lang);
            string laun = new GoogleTranslate().TranslateText(language, "ru", lang);
            BaseText bs = new BaseText(file,fileOpen, fileSave, fileSaveAs, run, runConsole, runCompiling, docs, report, view, examples, noInfo, err, saveFileForComp, fileNoFound, laun);
            return bs;
        }
    }
}
