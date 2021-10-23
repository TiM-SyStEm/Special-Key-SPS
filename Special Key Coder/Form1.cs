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

        private FastColoredTextBoxNS.Style GreenStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Green, null, FontStyle.Bold);
        private FastColoredTextBoxNS.Style LightCoralStyle = new FastColoredTextBoxNS.TextStyle(Brushes.LightCoral, null, FontStyle.Bold);
        private FastColoredTextBoxNS.Style LightBlueStyle = new FastColoredTextBoxNS.TextStyle(Brushes.LightBlue, null, FontStyle.Bold);
        private FastColoredTextBoxNS.Style BlueStyleCorect = new FastColoredTextBoxNS.TextStyle(Brushes.Blue, null, FontStyle.Regular);
        private FastColoredTextBoxNS.Style LightGreenStyle = new FastColoredTextBoxNS.TextStyle(Brushes.LightGreen, null, FontStyle.Regular);
        private FastColoredTextBoxNS.Style AddStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Orange, null, FontStyle.Underline);
        private FastColoredTextBoxNS.Style GoldenRodStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Goldenrod, null, FontStyle.Italic);
        private FastColoredTextBoxNS.Style TurquoiseStyle = new FastColoredTextBoxNS.TextStyle(Brushes.Turquoise, null, FontStyle.Regular);
        private FastColoredTextBoxNS.Style MediumPurpleStyle = new FastColoredTextBoxNS.TextStyle(Brushes.MediumPurple, null, FontStyle.Regular);

        public Form1()
        {
            InitializeComponent();
            autocompleteMenu1.Items = File.ReadAllLines("spk-reserv.dict");
            string[] allfiles = Directory.GetFiles("modules", "*.spk");
            for (int ind = 0; ind < allfiles.Length; ind++)
            {
                modules.Add(allfiles[ind].Split('\\')[1].Split('.')[0]);
            }
        }

        private void openToolStripMenuItem_Click(object sender, EventArgs e)
        {
            openFileDialog1.Filter = "Special Key source code (*.spk) | *.spk";
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
                saveFileDialog1.Filter = "Special Key source code (*.spk) | *.spk";
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
            saveFileDialog1.Filter = "Special Key source code (*.spk) | *.spk";
            if (saveFileDialog1.ShowDialog() != DialogResult.Cancel)
            {
                openfile = saveFileDialog1.FileName;
                File.WriteAllText(openfile, fastColoredTextBox1.Text, Encoding.ASCII);

                button1.Text = openfile;
                path = openfile;
            }
        }

        private void runConsoleToolStripMenuItem_Click(object sender, EventArgs e) => Process.Start("Compiler.exe");

        private void fastColoredTextBox1_TextChanged(object sender, FastColoredTextBoxNS.TextChangedEventArgs e)
        {
            try
            {
                e.ChangedRange.ClearFoldingMarkers();
                e.ChangedRange.SetFoldingMarkers("{", "}");
                e.ChangedRange.SetFoldingMarkers("<-", "->");

                e.ChangedRange.SetStyle(GreenStyle, @"#.*$");

                e.ChangedRange.SetStyle(GoldenRodStyle, @"fun");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"class");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"if");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"else if");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"for");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"while");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"else");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"stop");
                e.ChangedRange.SetStyle(GoldenRodStyle, @"continue");

                e.ChangedRange.SetStyle(TurquoiseStyle, @"toStr");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toInt");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toFloat");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toByte");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toShort");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toLong");
                e.ChangedRange.SetStyle(TurquoiseStyle, @"toDouble");

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
                MessageBox.Show("Colored ERROR!");
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Clipboard.Clear();
            Clipboard.SetText(button1.Text);
        }

        private void darkToolStripMenuItem_Click(object sender, EventArgs e)
        {
            fastColoredTextBox1.ForeColor = Color.Beige;
            fastColoredTextBox1.BackColor = ColorTranslator.FromHtml("#2e2f31");
            fastColoredTextBox1.ServiceLinesColor = ColorTranslator.FromHtml("#2e2f31");
            fastColoredTextBox1.IndentBackColor = ColorTranslator.FromHtml("#2e2f31");
            panel1.BackColor = ColorTranslator.FromHtml("#333333");
            menuStrip1.BackColor = ColorTranslator.FromHtml("#333");
            menuStrip1.ForeColor = ColorTranslator.FromHtml("#03c4a1");
            darkToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            darkToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            lightToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            lightToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            backgroundColorToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            backgroundColorToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            fontToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            fontToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            runConsoleToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            runConsoleToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            openToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            openToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            скопмилироватьToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#333333");
            скопмилироватьToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#03c4a1");
            saveToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            saveToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            saveAsToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#292a2c");
            saveAsToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#fff");
            всеМодулиToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#333333");
            всеМодулиToolStripMenuItem.ForeColor = ColorTranslator.FromHtml("#03c4a1");
            theme = 2;
        }

        private void lightToolStripMenuItem_Click(object sender, EventArgs e)
        {
            fastColoredTextBox1.ForeColor = Color.Black;
            fastColoredTextBox1.BackColor = Color.White;
            fastColoredTextBox1.IndentBackColor = Color.White;
            fastColoredTextBox1.ServiceLinesColor = Color.White;
            panel1.BackColor = ColorTranslator.FromHtml("#F0F0F0");
            menuStrip1.BackColor = ColorTranslator.FromHtml("#F0F0F0");
            menuStrip1.ForeColor = Color.Black;
            darkToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            darkToolStripMenuItem.ForeColor = Color.Black;
            lightToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            lightToolStripMenuItem.ForeColor = Color.Black;
            backgroundColorToolStripMenuItem.ForeColor = Color.Black;
            backgroundColorToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            fontToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            fontToolStripMenuItem.ForeColor = Color.Black;
            runConsoleToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            runConsoleToolStripMenuItem.ForeColor = Color.Black;
            openToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            openToolStripMenuItem.ForeColor = Color.Black;
            скопмилироватьToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            скопмилироватьToolStripMenuItem.ForeColor = Color.Black;
            saveToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            saveToolStripMenuItem.ForeColor = Color.Black;
            saveAsToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            saveAsToolStripMenuItem.ForeColor = Color.Black;
            всеМодулиToolStripMenuItem.BackColor = ColorTranslator.FromHtml("#FDFDFD");
            всеМодулиToolStripMenuItem.ForeColor = Color.Black;
            theme = 1;
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
        private void документацияToolStripMenuItem_Click(object sender, EventArgs e) => Process.Start("https://special-key.cf/tutorials.html");
        private void AddLib(string name) => fastColoredTextBox1.Text = "Add " + name + "\n" + fastColoredTextBox1.Text;
        private void stlToolStripMenuItem_Click(object sender, EventArgs e) => AddLib("stl");

        private void wSGcolorsToolStripMenuItem_Click(object sender, EventArgs e) => AddLib("WSGcolors");

        private void wSGkeysToolStripMenuItem_Click(object sender, EventArgs e) => AddLib("WSGkeys");

        private void wSGcanvasToolStripMenuItem_Click(object sender, EventArgs e) => AddLib("WSGcanvas");

        private void wSGformsToolStripMenuItem_Click(object sender, EventArgs e) =>  AddLib("WSGforms");

        private void всеМодулиToolStripMenuItem_Click(object sender, EventArgs e)
        {
            allmodules alm = new allmodules();
            alm.ShowDialog();
        }

        private void Form1_Activated(object sender, EventArgs e)
        {
            if (text != "" && text != fastColoredTextBox1.Text)
            {
                fastColoredTextBox1.Text = text;
            }
        }

        private void скопмилироватьToolStripMenuItem_Click(object sender, EventArgs e) 
        {
            if (button1.Text != "Нет информации об пути")
                Process.Start("Compiler.exe", "\"" + path + "\"");
            else
                MessageBox.Show("Сначала сохраните файл!", "Файл не найден");
        }
    }
}
