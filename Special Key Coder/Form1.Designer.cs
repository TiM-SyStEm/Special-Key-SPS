﻿
namespace Special_KeyCoder
{
    partial class Form1
    {
        /// <summary>
        /// Обязательная переменная конструктора.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Освободить все используемые ресурсы.
        /// </summary>
        /// <param name="disposing">истинно, если управляемый ресурс должен быть удален; иначе ложно.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Код, автоматически созданный конструктором форм Windows

        /// <summary>
        /// Требуемый метод для поддержки конструктора — не изменяйте 
        /// содержимое этого метода с помощью редактора кода.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Form1));
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.fileToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.saveToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.saveAsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.runToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.runConsoleToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.peToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.документацияToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.отчётToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.видToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.scrollbarsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.примерыToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
            this.fontDialog1 = new System.Windows.Forms.FontDialog();
            this.colorDialog1 = new System.Windows.Forms.ColorDialog();
            this.folderBrowserDialog1 = new System.Windows.Forms.FolderBrowserDialog();
            this.guna2Panel1 = new System.Windows.Forms.Panel();
            this.button1 = new System.Windows.Forms.Button();
            this.panel1 = new System.Windows.Forms.Panel();
            this.splitContainer1 = new System.Windows.Forms.SplitContainer();
            this.fastColoredTextBox1 = new FastColoredTextBoxNS.FastColoredTextBox();
            this.documentMap1 = new FastColoredTextBoxNS.DocumentMap();
            this.autocompleteMenu1 = new AutocompleteMenuNS.AutocompleteMenu();
            this.menuStrip1.SuspendLayout();
            this.guna2Panel1.SuspendLayout();
            this.panel1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).BeginInit();
            this.splitContainer1.Panel1.SuspendLayout();
            this.splitContainer1.Panel2.SuspendLayout();
            this.splitContainer1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.fastColoredTextBox1)).BeginInit();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.BackColor = System.Drawing.Color.SteelBlue;
            this.menuStrip1.ImageScalingSize = new System.Drawing.Size(20, 20);
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.runToolStripMenuItem,
            this.документацияToolStripMenuItem,
            this.отчётToolStripMenuItem,
            this.видToolStripMenuItem,
            this.примерыToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Padding = new System.Windows.Forms.Padding(4, 2, 0, 2);
            this.menuStrip1.Size = new System.Drawing.Size(762, 24);
            this.menuStrip1.TabIndex = 1;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openToolStripMenuItem,
            this.saveToolStripMenuItem,
            this.saveAsToolStripMenuItem});
            this.fileToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(43, 20);
            this.fileToolStripMenuItem.Text = "[file]";
            // 
            // openToolStripMenuItem
            // 
            this.openToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.openToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.O)));
            this.openToolStripMenuItem.Size = new System.Drawing.Size(207, 22);
            this.openToolStripMenuItem.Text = "[fileOpen]";
            this.openToolStripMenuItem.Click += new System.EventHandler(this.openToolStripMenuItem_Click);
            // 
            // saveToolStripMenuItem
            // 
            this.saveToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.saveToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.saveToolStripMenuItem.Name = "saveToolStripMenuItem";
            this.saveToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.S)));
            this.saveToolStripMenuItem.Size = new System.Drawing.Size(207, 22);
            this.saveToolStripMenuItem.Text = "[fileSave]";
            this.saveToolStripMenuItem.Click += new System.EventHandler(this.saveToolStripMenuItem_Click);
            // 
            // saveAsToolStripMenuItem
            // 
            this.saveAsToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.saveAsToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.saveAsToolStripMenuItem.Name = "saveAsToolStripMenuItem";
            this.saveAsToolStripMenuItem.ShortcutKeys = ((System.Windows.Forms.Keys)(((System.Windows.Forms.Keys.Control | System.Windows.Forms.Keys.Shift) 
            | System.Windows.Forms.Keys.S)));
            this.saveAsToolStripMenuItem.Size = new System.Drawing.Size(207, 22);
            this.saveAsToolStripMenuItem.Text = "[fileSaveAs]";
            this.saveAsToolStripMenuItem.Click += new System.EventHandler(this.saveAsToolStripMenuItem_Click);
            // 
            // runToolStripMenuItem
            // 
            this.runToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.runToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.runConsoleToolStripMenuItem,
            this.peToolStripMenuItem});
            this.runToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.runToolStripMenuItem.Name = "runToolStripMenuItem";
            this.runToolStripMenuItem.Size = new System.Drawing.Size(45, 20);
            this.runToolStripMenuItem.Text = "[run]";
            // 
            // runConsoleToolStripMenuItem
            // 
            this.runConsoleToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.runConsoleToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.runConsoleToolStripMenuItem.Name = "runConsoleToolStripMenuItem";
            this.runConsoleToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.runConsoleToolStripMenuItem.Text = "[runConsole]";
            this.runConsoleToolStripMenuItem.Click += new System.EventHandler(this.runConsoleToolStripMenuItem_Click);
            // 
            // peToolStripMenuItem
            // 
            this.peToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.peToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.peToolStripMenuItem.Name = "peToolStripMenuItem";
            this.peToolStripMenuItem.ShortcutKeys = System.Windows.Forms.Keys.F5;
            this.peToolStripMenuItem.Size = new System.Drawing.Size(180, 22);
            this.peToolStripMenuItem.Text = "[runCompiling]";
            this.peToolStripMenuItem.Click += new System.EventHandler(this.peToolStripMenuItem_Click);
            // 
            // документацияToolStripMenuItem
            // 
            this.документацияToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.документацияToolStripMenuItem.Name = "документацияToolStripMenuItem";
            this.документацияToolStripMenuItem.Size = new System.Drawing.Size(52, 20);
            this.документацияToolStripMenuItem.Text = "[docs]";
            this.документацияToolStripMenuItem.Click += new System.EventHandler(this.документацияToolStripMenuItem_Click);
            // 
            // отчётToolStripMenuItem
            // 
            this.отчётToolStripMenuItem.ForeColor = System.Drawing.SystemColors.ControlLight;
            this.отчётToolStripMenuItem.Name = "отчётToolStripMenuItem";
            this.отчётToolStripMenuItem.Size = new System.Drawing.Size(59, 20);
            this.отчётToolStripMenuItem.Text = "[report]";
            this.отчётToolStripMenuItem.Click += new System.EventHandler(this.отчётToolStripMenuItem_Click);
            // 
            // видToolStripMenuItem
            // 
            this.видToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.scrollbarsToolStripMenuItem});
            this.видToolStripMenuItem.ForeColor = System.Drawing.Color.White;
            this.видToolStripMenuItem.Name = "видToolStripMenuItem";
            this.видToolStripMenuItem.Size = new System.Drawing.Size(51, 20);
            this.видToolStripMenuItem.Text = "[view]";
            // 
            // scrollbarsToolStripMenuItem
            // 
            this.scrollbarsToolStripMenuItem.BackColor = System.Drawing.Color.SteelBlue;
            this.scrollbarsToolStripMenuItem.CheckOnClick = true;
            this.scrollbarsToolStripMenuItem.ForeColor = System.Drawing.Color.White;
            this.scrollbarsToolStripMenuItem.Name = "scrollbarsToolStripMenuItem";
            this.scrollbarsToolStripMenuItem.Size = new System.Drawing.Size(125, 22);
            this.scrollbarsToolStripMenuItem.Text = "Scrollbars";
            this.scrollbarsToolStripMenuItem.CheckedChanged += new System.EventHandler(this.scrollbarsToolStripMenuItem_CheckedChanged);
            // 
            // примерыToolStripMenuItem
            // 
            this.примерыToolStripMenuItem.ForeColor = System.Drawing.Color.White;
            this.примерыToolStripMenuItem.Name = "примерыToolStripMenuItem";
            this.примерыToolStripMenuItem.Size = new System.Drawing.Size(77, 20);
            this.примерыToolStripMenuItem.Text = "[examples]";
            this.примерыToolStripMenuItem.TextAlign = System.Drawing.ContentAlignment.TopCenter;
            this.примерыToolStripMenuItem.Click += new System.EventHandler(this.примерыToolStripMenuItem_Click);
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.Title = "Open *.spk";
            // 
            // guna2Panel1
            // 
            this.guna2Panel1.BackColor = System.Drawing.Color.SteelBlue;
            this.guna2Panel1.Controls.Add(this.button1);
            this.guna2Panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.guna2Panel1.Location = new System.Drawing.Point(0, 503);
            this.guna2Panel1.Margin = new System.Windows.Forms.Padding(2);
            this.guna2Panel1.Name = "guna2Panel1";
            this.guna2Panel1.Size = new System.Drawing.Size(762, 24);
            this.guna2Panel1.TabIndex = 13;
            // 
            // button1
            // 
            this.button1.BackColor = System.Drawing.Color.SteelBlue;
            this.button1.Cursor = System.Windows.Forms.Cursors.Hand;
            this.button1.FlatAppearance.BorderSize = 0;
            this.button1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.button1.ForeColor = System.Drawing.Color.YellowGreen;
            this.button1.Location = new System.Drawing.Point(0, -2);
            this.button1.Margin = new System.Windows.Forms.Padding(2);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(262, 24);
            this.button1.TabIndex = 0;
            this.button1.Text = "[noInfoAboutPath]";
            this.button1.UseVisualStyleBackColor = false;
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.splitContainer1);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panel1.Location = new System.Drawing.Point(0, 24);
            this.panel1.Margin = new System.Windows.Forms.Padding(2);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(762, 479);
            this.panel1.TabIndex = 14;
            // 
            // splitContainer1
            // 
            this.splitContainer1.BackColor = System.Drawing.Color.SteelBlue;
            this.splitContainer1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.splitContainer1.Location = new System.Drawing.Point(0, 0);
            this.splitContainer1.Margin = new System.Windows.Forms.Padding(2);
            this.splitContainer1.Name = "splitContainer1";
            // 
            // splitContainer1.Panel1
            // 
            this.splitContainer1.Panel1.Controls.Add(this.fastColoredTextBox1);
            // 
            // splitContainer1.Panel2
            // 
            this.splitContainer1.Panel2.Controls.Add(this.documentMap1);
            this.splitContainer1.Size = new System.Drawing.Size(762, 479);
            this.splitContainer1.SplitterDistance = 642;
            this.splitContainer1.SplitterWidth = 3;
            this.splitContainer1.TabIndex = 0;
           // this.splitContainer1.SplitterMoved += new System.Windows.Forms.SplitterEventHandler(this.splitContainer1_SplitterMoved);
            // 
            // fastColoredTextBox1
            // 
            this.fastColoredTextBox1.AutoCompleteBrackets = true;
            this.fastColoredTextBox1.AutoCompleteBracketsList = new char[] {
        '(',
        ')',
        '{',
        '}',
        '[',
        ']',
        '\"',
        '\"',
        '\'',
        '\''};
            this.autocompleteMenu1.SetAutocompleteMenu(this.fastColoredTextBox1, this.autocompleteMenu1);
            this.fastColoredTextBox1.AutoIndentCharsPatterns = "^\\s*[\\w\\.]+(\\s\\w+)?\\s*(?<range>=)\\s*(?<range>[^;=]+);\r\n^\\s*(case|default)\\s*[^:]*" +
    "(?<range>:)\\s*(?<range>[^;]+);";
            this.fastColoredTextBox1.AutoScrollMinSize = new System.Drawing.Size(27, 14);
            this.fastColoredTextBox1.BackBrush = null;
            this.fastColoredTextBox1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.fastColoredTextBox1.CharHeight = 14;
            this.fastColoredTextBox1.CharWidth = 8;
            this.fastColoredTextBox1.CommentPrefix = "#*";
            this.fastColoredTextBox1.Cursor = System.Windows.Forms.Cursors.Arrow;
            this.fastColoredTextBox1.DisabledColor = System.Drawing.Color.FromArgb(((int)(((byte)(100)))), ((int)(((byte)(180)))), ((int)(((byte)(180)))), ((int)(((byte)(180)))));
            this.fastColoredTextBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.fastColoredTextBox1.FoldingIndicatorColor = System.Drawing.Color.DodgerBlue;
            this.fastColoredTextBox1.Font = new System.Drawing.Font("Courier New", 9.75F);
            this.fastColoredTextBox1.ForeColor = System.Drawing.Color.White;
            this.fastColoredTextBox1.IndentBackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.fastColoredTextBox1.IsReplaceMode = false;
            this.fastColoredTextBox1.LineNumberColor = System.Drawing.Color.DodgerBlue;
            this.fastColoredTextBox1.Location = new System.Drawing.Point(0, 0);
            this.fastColoredTextBox1.Margin = new System.Windows.Forms.Padding(2);
            this.fastColoredTextBox1.Name = "fastColoredTextBox1";
            this.fastColoredTextBox1.Paddings = new System.Windows.Forms.Padding(0);
            this.fastColoredTextBox1.SelectionColor = System.Drawing.Color.FromArgb(((int)(((byte)(60)))), ((int)(((byte)(0)))), ((int)(((byte)(120)))), ((int)(((byte)(215)))));
            this.fastColoredTextBox1.ServiceColors = null;
            this.fastColoredTextBox1.ServiceLinesColor = System.Drawing.Color.SlateGray;
            this.fastColoredTextBox1.ShowScrollBars = false;
            this.fastColoredTextBox1.Size = new System.Drawing.Size(642, 479);
            this.fastColoredTextBox1.TabIndex = 13;
            this.fastColoredTextBox1.Zoom = 100;
            this.fastColoredTextBox1.TextChanged += new System.EventHandler<FastColoredTextBoxNS.TextChangedEventArgs>(this.fastColoredTextBox1_TextChanged);
            // 
            // documentMap1
            // 
            this.documentMap1.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(64)))), ((int)(((byte)(64)))), ((int)(((byte)(64)))));
            this.documentMap1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.documentMap1.ForeColor = System.Drawing.Color.Maroon;
            this.documentMap1.Location = new System.Drawing.Point(0, 0);
            this.documentMap1.Margin = new System.Windows.Forms.Padding(2);
            this.documentMap1.Name = "documentMap1";
            this.documentMap1.Scale = 0.75F;
            this.documentMap1.Size = new System.Drawing.Size(117, 479);
            this.documentMap1.TabIndex = 3;
            this.documentMap1.Target = this.fastColoredTextBox1;
            this.documentMap1.Text = "fffffff";
            // 
            // autocompleteMenu1
            // 
            this.autocompleteMenu1.AllowsTabKey = true;
            this.autocompleteMenu1.Colors = ((AutocompleteMenuNS.Colors)(resources.GetObject("autocompleteMenu1.Colors")));
            this.autocompleteMenu1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
            this.autocompleteMenu1.ImageList = null;
            this.autocompleteMenu1.Items = new string[0];
            this.autocompleteMenu1.MinFragmentLength = 1;
            this.autocompleteMenu1.TargetControlWrapper = null;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(762, 527);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.guna2Panel1);
            this.Controls.Add(this.menuStrip1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MainMenuStrip = this.menuStrip1;
            this.Margin = new System.Windows.Forms.Padding(2);
            this.Name = "Form1";
            this.Text = "Special Key Coder IDE";
            this.Activated += new System.EventHandler(this.Form1_Activated);
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.guna2Panel1.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
            this.splitContainer1.Panel1.ResumeLayout(false);
            this.splitContainer1.Panel2.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.splitContainer1)).EndInit();
            this.splitContainer1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.fastColoredTextBox1)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem fileToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem openToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem saveToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem saveAsToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem runToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem runConsoleToolStripMenuItem;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.Windows.Forms.SaveFileDialog saveFileDialog1;
        private System.Windows.Forms.FontDialog fontDialog1;
        private System.Windows.Forms.ColorDialog colorDialog1;
        private System.Windows.Forms.ToolStripMenuItem документацияToolStripMenuItem;
        private System.Windows.Forms.FolderBrowserDialog folderBrowserDialog1;
        private System.Windows.Forms.ToolStripMenuItem отчётToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem peToolStripMenuItem;
        private System.Windows.Forms.Panel guna2Panel1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Panel panel1;
        private AutocompleteMenuNS.AutocompleteMenu autocompleteMenu1;
        private System.Windows.Forms.SplitContainer splitContainer1;
        private FastColoredTextBoxNS.FastColoredTextBox fastColoredTextBox1;
        private FastColoredTextBoxNS.DocumentMap documentMap1;
        private System.Windows.Forms.ToolStripMenuItem видToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem scrollbarsToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem примерыToolStripMenuItem;
    }
}

