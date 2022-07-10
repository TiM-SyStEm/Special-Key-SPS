using System.IO;
using System.Collections.Generic;
using System.Windows.Forms;

namespace Special_Key_Coder
{
    class LangTextLoader
    {
        public static BaseText Start()
        {
            string lang = File.ReadAllText("SaveLangIs.txt");
            return main_loader(lang + ".txt");
        }
        protected static BaseText main_loader(string file)
        {
            try
            {
                string[] txt = File.ReadAllText(file).Replace("\r", "").Replace("\n", "").Split(';');
                List<string> a = new List<string> { };
                foreach (string line in txt) a.Add(line);
                string[] a_array = a.ToArray();
                BaseText bs = new BaseText(a_array[0], a_array[1], a_array[2], a_array[3], a_array[4], a_array[5], a_array[6],
                    a_array[7], a_array[8], a_array[9], a_array[10], a_array[11], a_array[12], a_array[13], a_array[14], a_array[15]);
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
                    a_array[7], a_array[8], a_array[9], a_array[10], a_array[11], a_array[12], a_array[13], a_array[14], a_array[15]);
                File.WriteAllText("SaveLangIs.txt", "en");
                return bs;
            }
        }
    }
}
