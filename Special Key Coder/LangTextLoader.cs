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
            try
            {
                string[] txt = File.ReadAllText(file).Replace("\r", "").Replace("\n", "").Split(';');
                List<string> a = new List<string> { };
                foreach (string line in txt) a.Add(line);
                string[] a_array = a.ToArray();
                BaseText bs = new BaseText(a_array[0], a_array[1], a_array[2], a_array[3], a_array[4], a_array[5], a_array[6],
                    a_array[7], a_array[8], a_array[9], a_array[10], a_array[11], a_array[12], a_array[13], a_array[14], a_array[15], a_array[16]);
                return bs;
            }
            catch(System.Exception ex)
            {
                MessageBox.Show( "(" + file.Replace(".txt", "") + ") language package is not found. Default language package (en) will be installed.");
                string[] txt = File.ReadAllText("en.txt").Replace("\r", "").Replace("\n", "").Split(';');
                List<string> a = new List<string> { };
                foreach (string line in txt) a.Add(line);
                string[] a_array = a.ToArray();
                BaseText bs = new BaseText(a_array[0], a_array[1], a_array[2], a_array[3], a_array[4], a_array[5], a_array[6],
                    a_array[7], a_array[8], a_array[9], a_array[10], a_array[11], a_array[12], a_array[13], a_array[14], a_array[15], a_array[16]);
                File.WriteAllText("SaveLangIs.txt", "en");
                return bs;
            }
        }
    }
}
