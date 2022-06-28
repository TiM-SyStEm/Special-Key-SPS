﻿using Special_Key_Coder;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Drawing;
using System.IO;
using System.Text;
using System.Windows.Forms;

namespace Special_KeyCoder
{
    public partial class Form1 : Form
    {
        public string openfile = string.Empty;
        public static int theme = 1;
        public static List<string> modules = new List<string> { };
        public static string text = string.Empty;
        public static string path = string.Empty;
        public static string srhfile = string.Empty;

        private FastColoredTextBoxNS.Style GreenStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Green, null, FontStyle.Bold);
        private FastColoredTextBoxNS.Style LightCoralStyle = new FastColoredTextBoxNS.TextStyle(Brushes.LightCoral, null, FontStyle.Bold);
        private FastColoredTextBoxNS.Style LightBlueStyle = new FastColoredTextBoxNS.TextStyle(Brushes.LightBlue, null, FontStyle.Bold);
        private FastColoredTextBoxNS.Style BlueStyleCorect = new FastColoredTextBoxNS.TextStyle(Brushes.Blue, null, FontStyle.Regular);
        private FastColoredTextBoxNS.Style LightGreenStyle = new FastColoredTextBoxNS.TextStyle(Brushes.LightGreen, null, FontStyle.Regular);
        private FastColoredTextBoxNS.Style AddStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Orange, null, FontStyle.Underline);
        private FastColoredTextBoxNS.Style GoldenRodStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Goldenrod, null, FontStyle.Italic);
        private FastColoredTextBoxNS.Style TurquoiseStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Turquoise, null, FontStyle.Regular);
        private FastColoredTextBoxNS.Style MediumPurpleStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Purple, null, FontStyle.Regular);
        public static BaseText bs;
        public Form1()
        {
            InitializeComponent();
            if (File.ReadAllText("SaveLangIs.txt") == "")
            {
                var culture = System.Globalization.CultureInfo.CurrentCulture;
                File.WriteAllText("SaveLangIs.txt", culture.ToString().Split('-')[0]);
            }
            bs = LangTextLoader.Start();
            autocompleteMenu1.Items = File.ReadAllLines("spk-reserv.dict");
            string[] allfiles = Directory.GetFiles("modules", "*.spk");
            for (int ind = 0; ind < allfiles.Length; ind++)
            {
                modules.Add(allfiles[ind].Split('\\')[1].Split('.')[0]);
            }

            // Change text on Form1
            fileToolStripMenuItem.Text = bs.file;
            openToolStripMenuItem.Text = bs.fileOpen;
            saveToolStripMenuItem.Text = bs.fileSave;
            saveAsToolStripMenuItem.Text = bs.fileSaveAs;
            runToolStripMenuItem.Text = bs.run;
            runConsoleToolStripMenuItem.Text = bs.runConsole;
            peToolStripMenuItem.Text = bs.runCompiling;
            документацияToolStripMenuItem.Text = bs.docs;
            отчётToolStripMenuItem.Text = bs.report;
            видToolStripMenuItem.Text = bs.view;
            примерыToolStripMenuItem.Text = bs.examples;
            button1.Text = bs.noInfoAboutPath;
        }

        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            //openFileDialog1.Filter = "Special Key source code (*.spk) | *.spk |Sperhino SavedData (*.srh) | *.srh";
            if (openFileDialog1.ShowDialog() != DialogResult.Cancel)
            {
                openfile = openFileDialog1.FileName;
                path = openfile;
                fastColoredTextBox1.Text = File.ReadAllText(openfile, Encoding.ASCII);
                button1.Text = openfile;
            }
        }

        private void saveToolStripMenuItem_Click(object sender, EventArgs e)
        {
            try
            {
                File.WriteAllText(openfile, fastColoredTextBox1.Text, Encoding.ASCII);
                button1.Text = openfile;
                path = openfile;
            }
            catch (Exception)
            {
                MessageBox.Show("Файл не найден!");
                saveFileDialog1.Filter = "Special Key source code (*.spk) | *.spk | Sperhino SavedData (*.srh) | *.srh";
                if (saveFileDialog1.ShowDialog() == DialogResult.Cancel)
                {
                    return;
                }

                openfile = saveFileDialog1.FileName;
                File.WriteAllText(openfile, fastColoredTextBox1.Text, Encoding.ASCII);
                button1.Text = openfile;
                path = openfile;
            }
        }

        private void saveAsToolStripMenuItem_Click(object sender, EventArgs e)
        {
            saveFileDialog1.Filter = "Special Key source code (*.spk) | *.spk | Sperhino SavedData (*.srh) | *.srh";
            if (saveFileDialog1.ShowDialog() != DialogResult.Cancel)
            {
                openfile = saveFileDialog1.FileName;
                File.WriteAllText(openfile, fastColoredTextBox1.Text, Encoding.ASCII);

                button1.Text = openfile;
                path = openfile;
            }
        }

        private void runConsoleToolStripMenuItem_Click(object sender, EventArgs e) => Process.Start("SpecialKey.exe");

        private void fastColoredTextBox1_TextChanged(object sender, FastColoredTextBoxNS.TextChangedEventArgs e)
        {
            try
            {
                e.ChangedRange.ClearFoldingMarkers();
               // e.ChangedRange.SetFoldingMarkers("{", "}");

                e.ChangedRange.SetStyle(GreenStyle, @"#.*$");

                e.ChangedRange.SetStyle(GoldenRodStyle, @"fun");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"class");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"if");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"for");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"while");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"else");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"stop");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"continue");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"switch");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"case");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"enum");

                e.ChangedRange.SetStyle(TurquoiseStyle, @"toStr");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toInt");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toFloat");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toByte");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toShort");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toLong");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toDouble");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"ref");

                e.ChangedRange.SetStyle(BlueStyleCorect, @"length");

                e.ChangedRange.SetStyle(LightBlueStyle, @"Null");
                e.ChangedRange.SetStyle(LightBlueStyle, @"True");
                e.ChangedRange.SetStyle(LightBlueStyle, @"False");
                e.ChangedRange.SetStyle(LightGreenStyle, @"protected:");
                e.ChangedRange.SetStyle(LightGreenStyle, @"private:");

                e.ChangedRange.SetStyle(MediumPurpleStyle, @"input");
                e.ChangedRange.SetStyle(MediumPurpleStyle, @"out");

                e.ChangedRange.SetStyle(AddStyle, @"Add.*$");

                e.ChangedRange.SetStyle(LightCoralStyle, @"var");
            }
            catch (Exception)
            {
                MessageBox.Show(bs.coloredError);
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Clipboard.Clear();
            Clipboard.SetText(button1.Text);
        }
        private void fontToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (fontDialog1.ShowDialog() != DialogResult.Cancel)
            {
                fastColoredTextBox1.Font = fontDialog1.Font;
            }
        }

        private void backgroundColorToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (colorDialog1.ShowDialog() != DialogResult.Cancel)
            {
                fastColoredTextBox1.BackColor = colorDialog1.Color;
            }
        }

        private void скомпилироватьПроектToolStripMenuItem_Click(object sender, EventArgs e) => Process.Start("Compiler.exe");
        private void документацияToolStripMenuItem_Click(object sender, EventArgs e) => Process.Start("https://github.com/TiM-SyStEm/Special-Key-SPS/wiki");

        private void Form1_Activated(object sender, EventArgs e)
        {
            if (text != "" && text != fastColoredTextBox1.Text)
            {
                fastColoredTextBox1.Text = text;
            }
        }

        private void отчётToolStripMenuItem_Click(object sender, EventArgs e)
        {
            string log = File.ReadAllText("log.txt");
            if (log != "")
            {
                MessageBox.Show(log);
                File.WriteAllText("log.txt", "");
            }
            else
            {
                MessageBox.Show("0 errors");
            }
        }

        private void peToolStripMenuItem_Click(object sender, EventArgs e)
        {
            if (button1.Text != bs.noInfoAboutPath)
            {
                Process.Start("SpecialKey.exe", "\"" + path + "\"");
            }
            else
                MessageBox.Show(bs.inFirstSaveFile, bs.fileNotFound);
        }

        private void scrollbarsToolStripMenuItem_CheckedChanged(object sender, EventArgs e)
        {
            if (scrollbarsToolStripMenuItem.CheckState == CheckState.Checked) fastColoredTextBox1.ShowScrollBars = true;
            else fastColoredTextBox1.ShowScrollBars = false;
        }

        private void примерыToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Process.Start("https://github.com/TiM-SyStEm/Special-Key-SPS/tree/main/examples");
        }
    }
}
