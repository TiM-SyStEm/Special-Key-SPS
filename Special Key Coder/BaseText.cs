namespace Special_Key_Coder
{
    public class BaseText
    {
        public string file;
        public string fileOpen;
        public string fileSave;
        public string fileSaveAs;
        public string run;
        public string runConsole;
        public string runCompiling;
        public string docs;
        public string report;
        public string view;
        public string examples;
        public string noInfoAboutPath;
        public string coloredError;
        public string inFirstSaveFile;
        public string fileNotFound;
        public string launge;
        public string newFile;

        public BaseText(string file, string fileOpen, string fileSave,
        string fileSaveAs, string run, string runConsole, string runCompiling, string docs, string report, string view, string examples,
        string noInfoAboutPath, string coloredError, string inFirstSaveFile, string fileNotFound, string launge, string newFile)
        {
            this.file = file;
            this.fileOpen = fileOpen;
            this.fileSave = fileSave;
            this.fileSaveAs = fileSaveAs;
            this.run = run;
            this.runConsole = runConsole;
            this.runCompiling = runCompiling;
            this.docs = docs;
            this.report = report;
            this.view = view;
            this.examples = examples;
            this.noInfoAboutPath = noInfoAboutPath;
            this.coloredError = coloredError;
            this.inFirstSaveFile = inFirstSaveFile;
            this.fileNotFound = fileNotFound;
            this.launge = launge;
            this.newFile = newFile;
        }
    }
}
