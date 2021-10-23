
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
            this.themeToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.darkToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.lightToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.backgroundColorToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.fontToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.документацияToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.всеМодулиToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.скопмилироватьToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.saveFileDialog1 = new System.Windows.Forms.SaveFileDialog();
            this.autocompleteMenu1 = new AutocompleteMenuNS.AutocompleteMenu();
            this.fastColoredTextBox1 = new FastColoredTextBoxNS.FastColoredTextBox();
            this.contextMenuStrip1 = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.добавитьToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.stlToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.wSGcolorsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.wSGkeysToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.wSGcanvasToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.wSGformsToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.panel1 = new System.Windows.Forms.Panel();
            this.button1 = new System.Windows.Forms.Button();
            this.fontDialog1 = new System.Windows.Forms.FontDialog();
            this.colorDialog1 = new System.Windows.Forms.ColorDialog();
            this.menuStrip1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.fastColoredTextBox1)).BeginInit();
            this.contextMenuStrip1.SuspendLayout();
            this.panel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // menuStrip1
            // 
            this.menuStrip1.ImageScalingSize = new System.Drawing.Size(20, 20);
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.fileToolStripMenuItem,
            this.runToolStripMenuItem,
            this.themeToolStripMenuItem,
            this.документацияToolStripMenuItem,
            this.всеМодулиToolStripMenuItem,
            this.скопмилироватьToolStripMenuItem});
            this.menuStrip1.Location = new System.Drawing.Point(0, 0);
            this.menuStrip1.Name = "menuStrip1";
            this.menuStrip1.Padding = new System.Windows.Forms.Padding(5, 2, 0, 2);
            this.menuStrip1.Size = new System.Drawing.Size(1016, 28);
            this.menuStrip1.TabIndex = 1;
            this.menuStrip1.Text = "menuStrip1";
            // 
            // fileToolStripMenuItem
            // 
            this.fileToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openToolStripMenuItem,
            this.saveToolStripMenuItem,
            this.saveAsToolStripMenuItem});
            this.fileToolStripMenuItem.Name = "fileToolStripMenuItem";
            this.fileToolStripMenuItem.Size = new System.Drawing.Size(59, 24);
            this.fileToolStripMenuItem.Text = "Файл";
            // 
            // openToolStripMenuItem
            // 
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.Size = new System.Drawing.Size(192, 26);
            this.openToolStripMenuItem.Text = "Открыть";
            this.openToolStripMenuItem.Click += new System.EventHandler(this.openToolStripMenuItem_Click);
            // 
            // saveToolStripMenuItem
            // 
            this.saveToolStripMenuItem.Name = "saveToolStripMenuItem";
            this.saveToolStripMenuItem.Size = new System.Drawing.Size(192, 26);
            this.saveToolStripMenuItem.Text = "Сохранить";
            this.saveToolStripMenuItem.Click += new System.EventHandler(this.saveToolStripMenuItem_Click);
            // 
            // saveAsToolStripMenuItem
            // 
            this.saveAsToolStripMenuItem.Name = "saveAsToolStripMenuItem";
            this.saveAsToolStripMenuItem.Size = new System.Drawing.Size(192, 26);
            this.saveAsToolStripMenuItem.Text = "Сохранить как";
            this.saveAsToolStripMenuItem.Click += new System.EventHandler(this.saveAsToolStripMenuItem_Click);
            // 
            // runToolStripMenuItem
            // 
            this.runToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.runConsoleToolStripMenuItem});
            this.runToolStripMenuItem.Name = "runToolStripMenuItem";
            this.runToolStripMenuItem.Size = new System.Drawing.Size(91, 24);
            this.runToolStripMenuItem.Text = "Запустить";
            // 
            // runConsoleToolStripMenuItem
            // 
            this.runConsoleToolStripMenuItem.Name = "runConsoleToolStripMenuItem";
            this.runConsoleToolStripMenuItem.Size = new System.Drawing.Size(221, 26);
            this.runConsoleToolStripMenuItem.Text = "Запустить консоль";
            this.runConsoleToolStripMenuItem.Click += new System.EventHandler(this.runConsoleToolStripMenuItem_Click);
            // 
            // themeToolStripMenuItem
            // 
            this.themeToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.darkToolStripMenuItem,
            this.lightToolStripMenuItem,
            this.backgroundColorToolStripMenuItem,
            this.fontToolStripMenuItem});
            this.themeToolStripMenuItem.Name = "themeToolStripMenuItem";
            this.themeToolStripMenuItem.Size = new System.Drawing.Size(58, 24);
            this.themeToolStripMenuItem.Text = "Тема";
            // 
            // darkToolStripMenuItem
            // 
            this.darkToolStripMenuItem.Name = "darkToolStripMenuItem";
            this.darkToolStripMenuItem.Size = new System.Drawing.Size(193, 26);
            this.darkToolStripMenuItem.Text = "Тёмная";
            this.darkToolStripMenuItem.Click += new System.EventHandler(this.darkToolStripMenuItem_Click);
            // 
            // lightToolStripMenuItem
            // 
            this.lightToolStripMenuItem.Name = "lightToolStripMenuItem";
            this.lightToolStripMenuItem.Size = new System.Drawing.Size(193, 26);
            this.lightToolStripMenuItem.Text = "Светлая";
            this.lightToolStripMenuItem.Click += new System.EventHandler(this.lightToolStripMenuItem_Click);
            // 
            // backgroundColorToolStripMenuItem
            // 
            this.backgroundColorToolStripMenuItem.Name = "backgroundColorToolStripMenuItem";
            this.backgroundColorToolStripMenuItem.Size = new System.Drawing.Size(193, 26);
            this.backgroundColorToolStripMenuItem.Text = "Фоновый цвет";
            this.backgroundColorToolStripMenuItem.Click += new System.EventHandler(this.backgroundColorToolStripMenuItem_Click);
            // 
            // fontToolStripMenuItem
            // 
            this.fontToolStripMenuItem.Name = "fontToolStripMenuItem";
            this.fontToolStripMenuItem.Size = new System.Drawing.Size(193, 26);
            this.fontToolStripMenuItem.Text = "Шрифт";
            this.fontToolStripMenuItem.Click += new System.EventHandler(this.fontToolStripMenuItem_Click);
            // 
            // документацияToolStripMenuItem
            // 
            this.документацияToolStripMenuItem.Name = "документацияToolStripMenuItem";
            this.документацияToolStripMenuItem.Size = new System.Drawing.Size(124, 24);
            this.документацияToolStripMenuItem.Text = "Документация";
            this.документацияToolStripMenuItem.Click += new System.EventHandler(this.документацияToolStripMenuItem_Click);
            // 
            // всеМодулиToolStripMenuItem
            // 
            this.всеМодулиToolStripMenuItem.Name = "всеМодулиToolStripMenuItem";
            this.всеМодулиToolStripMenuItem.Size = new System.Drawing.Size(103, 24);
            this.всеМодулиToolStripMenuItem.Text = "Все модули";
            this.всеМодулиToolStripMenuItem.Click += new System.EventHandler(this.всеМодулиToolStripMenuItem_Click);
            // 
            // скопмилироватьToolStripMenuItem
            // 
            this.скопмилироватьToolStripMenuItem.Name = "скопмилироватьToolStripMenuItem";
            this.скопмилироватьToolStripMenuItem.Size = new System.Drawing.Size(142, 24);
            this.скопмилироватьToolStripMenuItem.Text = "Скомпилировать";
            this.скопмилироватьToolStripMenuItem.Click += new System.EventHandler(this.скопмилироватьToolStripMenuItem_Click);
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.Title = "Open *.spk";
            // 
            // autocompleteMenu1
            // 
            this.autocompleteMenu1.Colors = ((AutocompleteMenuNS.Colors)(resources.GetObject("autocompleteMenu1.Colors")));
            this.autocompleteMenu1.Font = new System.Drawing.Font("Microsoft Sans Serif", 9F);
            this.autocompleteMenu1.ImageList = null;
            this.autocompleteMenu1.Items = new string[0];
            this.autocompleteMenu1.MaximumSize = new System.Drawing.Size(200, 230);
            this.autocompleteMenu1.MinFragmentLength = 1;
            this.autocompleteMenu1.TargetControlWrapper = null;
            this.autocompleteMenu1.ToolTipDuration = 100;
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
            this.fastColoredTextBox1.AutoScrollMinSize = new System.Drawing.Size(31, 18);
            this.fastColoredTextBox1.BackBrush = null;
            this.fastColoredTextBox1.CharHeight = 18;
            this.fastColoredTextBox1.CharWidth = 10;
            this.fastColoredTextBox1.CommentPrefix = "#*";
            this.fastColoredTextBox1.ContextMenuStrip = this.contextMenuStrip1;
            this.fastColoredTextBox1.Cursor = System.Windows.Forms.Cursors.IBeam;
            this.fastColoredTextBox1.DisabledColor = System.Drawing.Color.FromArgb(((int)(((byte)(100)))), ((int)(((byte)(180)))), ((int)(((byte)(180)))), ((int)(((byte)(180)))));
            this.fastColoredTextBox1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.fastColoredTextBox1.FoldingIndicatorColor = System.Drawing.Color.DodgerBlue;
            this.fastColoredTextBox1.Font = new System.Drawing.Font("Courier New", 9.75F);
            this.fastColoredTextBox1.IndentBackColor = System.Drawing.Color.White;
            this.fastColoredTextBox1.IsReplaceMode = false;
            this.fastColoredTextBox1.LineNumberColor = System.Drawing.Color.DodgerBlue;
            this.fastColoredTextBox1.Location = new System.Drawing.Point(0, 28);
            this.fastColoredTextBox1.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.fastColoredTextBox1.Name = "fastColoredTextBox1";
            this.fastColoredTextBox1.Paddings = new System.Windows.Forms.Padding(0);
            this.fastColoredTextBox1.SelectionColor = System.Drawing.Color.FromArgb(((int)(((byte)(60)))), ((int)(((byte)(0)))), ((int)(((byte)(120)))), ((int)(((byte)(215)))));
            this.fastColoredTextBox1.ServiceColors = ((FastColoredTextBoxNS.ServiceColors)(resources.GetObject("fastColoredTextBox1.ServiceColors")));
            this.fastColoredTextBox1.ServiceLinesColor = System.Drawing.Color.White;
            this.fastColoredTextBox1.Size = new System.Drawing.Size(1016, 589);
            this.fastColoredTextBox1.TabIndex = 4;
            this.fastColoredTextBox1.Zoom = 100;
            this.fastColoredTextBox1.TextChanged += new System.EventHandler<FastColoredTextBoxNS.TextChangedEventArgs>(this.fastColoredTextBox1_TextChanged);
            // 
            // contextMenuStrip1
            // 
            this.contextMenuStrip1.ImageScalingSize = new System.Drawing.Size(20, 20);
            this.contextMenuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.добавитьToolStripMenuItem});
            this.contextMenuStrip1.Name = "contextMenuStrip1";
            this.contextMenuStrip1.Size = new System.Drawing.Size(146, 28);
            // 
            // добавитьToolStripMenuItem
            // 
            this.добавитьToolStripMenuItem.BackColor = System.Drawing.SystemColors.Highlight;
            this.добавитьToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.stlToolStripMenuItem,
            this.wSGcolorsToolStripMenuItem,
            this.wSGkeysToolStripMenuItem,
            this.wSGcanvasToolStripMenuItem,
            this.wSGformsToolStripMenuItem});
            this.добавитьToolStripMenuItem.ForeColor = System.Drawing.SystemColors.Control;
            this.добавитьToolStripMenuItem.Name = "добавитьToolStripMenuItem";
            this.добавитьToolStripMenuItem.Size = new System.Drawing.Size(145, 24);
            this.добавитьToolStripMenuItem.Text = "Добавить";
            // 
            // stlToolStripMenuItem
            // 
            this.stlToolStripMenuItem.BackColor = System.Drawing.SystemColors.Highlight;
            this.stlToolStripMenuItem.ForeColor = System.Drawing.SystemColors.Control;
            this.stlToolStripMenuItem.Name = "stlToolStripMenuItem";
            this.stlToolStripMenuItem.Size = new System.Drawing.Size(168, 26);
            this.stlToolStripMenuItem.Text = "stl";
            this.stlToolStripMenuItem.Click += new System.EventHandler(this.stlToolStripMenuItem_Click);
            // 
            // wSGcolorsToolStripMenuItem
            // 
            this.wSGcolorsToolStripMenuItem.BackColor = System.Drawing.SystemColors.Highlight;
            this.wSGcolorsToolStripMenuItem.ForeColor = System.Drawing.SystemColors.Control;
            this.wSGcolorsToolStripMenuItem.Name = "wSGcolorsToolStripMenuItem";
            this.wSGcolorsToolStripMenuItem.Size = new System.Drawing.Size(168, 26);
            this.wSGcolorsToolStripMenuItem.Text = "WSGcolors";
            this.wSGcolorsToolStripMenuItem.Click += new System.EventHandler(this.wSGcolorsToolStripMenuItem_Click);
            // 
            // wSGkeysToolStripMenuItem
            // 
            this.wSGkeysToolStripMenuItem.BackColor = System.Drawing.SystemColors.Highlight;
            this.wSGkeysToolStripMenuItem.ForeColor = System.Drawing.SystemColors.Control;
            this.wSGkeysToolStripMenuItem.Name = "wSGkeysToolStripMenuItem";
            this.wSGkeysToolStripMenuItem.Size = new System.Drawing.Size(168, 26);
            this.wSGkeysToolStripMenuItem.Text = "WSGkeys";
            this.wSGkeysToolStripMenuItem.Click += new System.EventHandler(this.wSGkeysToolStripMenuItem_Click);
            // 
            // wSGcanvasToolStripMenuItem
            // 
            this.wSGcanvasToolStripMenuItem.BackColor = System.Drawing.SystemColors.Highlight;
            this.wSGcanvasToolStripMenuItem.ForeColor = System.Drawing.SystemColors.Control;
            this.wSGcanvasToolStripMenuItem.Name = "wSGcanvasToolStripMenuItem";
            this.wSGcanvasToolStripMenuItem.Size = new System.Drawing.Size(168, 26);
            this.wSGcanvasToolStripMenuItem.Text = "WSGcanvas";
            this.wSGcanvasToolStripMenuItem.Click += new System.EventHandler(this.wSGcanvasToolStripMenuItem_Click);
            // 
            // wSGformsToolStripMenuItem
            // 
            this.wSGformsToolStripMenuItem.BackColor = System.Drawing.SystemColors.Highlight;
            this.wSGformsToolStripMenuItem.ForeColor = System.Drawing.SystemColors.Control;
            this.wSGformsToolStripMenuItem.Name = "wSGformsToolStripMenuItem";
            this.wSGformsToolStripMenuItem.Size = new System.Drawing.Size(168, 26);
            this.wSGformsToolStripMenuItem.Text = "WSGforms";
            this.wSGformsToolStripMenuItem.Click += new System.EventHandler(this.wSGformsToolStripMenuItem_Click);
            // 
            // panel1
            // 
            this.panel1.Controls.Add(this.button1);
            this.panel1.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panel1.Location = new System.Drawing.Point(0, 617);
            this.panel1.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.panel1.Name = "panel1";
            this.panel1.Size = new System.Drawing.Size(1016, 32);
            this.panel1.TabIndex = 2;
            // 
            // button1
            // 
            this.button1.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
            this.button1.Cursor = System.Windows.Forms.Cursors.Hand;
            this.button1.FlatAppearance.BorderSize = 0;
            this.button1.FlatStyle = System.Windows.Forms.FlatStyle.Flat;
            this.button1.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(96)))), ((int)(((byte)(20)))));
            this.button1.Location = new System.Drawing.Point(663, 0);
            this.button1.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(349, 30);
            this.button1.TabIndex = 0;
            this.button1.Text = "Нет информации об пути";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1016, 649);
            this.Controls.Add(this.fastColoredTextBox1);
            this.Controls.Add(this.panel1);
            this.Controls.Add(this.menuStrip1);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.MainMenuStrip = this.menuStrip1;
            this.Margin = new System.Windows.Forms.Padding(3, 2, 3, 2);
            this.Name = "Form1";
            this.Text = "Special Key Coder IDE";
            this.Activated += new System.EventHandler(this.Form1_Activated);
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.fastColoredTextBox1)).EndInit();
            this.contextMenuStrip1.ResumeLayout(false);
            this.panel1.ResumeLayout(false);
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
        private AutocompleteMenuNS.AutocompleteMenu autocompleteMenu1;
        private System.Windows.Forms.Panel panel1;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.ToolStripMenuItem themeToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem darkToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem lightToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem backgroundColorToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem fontToolStripMenuItem;
        private System.Windows.Forms.FontDialog fontDialog1;
        private System.Windows.Forms.ColorDialog colorDialog1;
        private FastColoredTextBoxNS.FastColoredTextBox fastColoredTextBox1;
        private System.Windows.Forms.ContextMenuStrip contextMenuStrip1;
        private System.Windows.Forms.ToolStripMenuItem добавитьToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem stlToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem wSGcolorsToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem wSGkeysToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem wSGcanvasToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem wSGformsToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem документацияToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem всеМодулиToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem скопмилироватьToolStripMenuItem;
    }
}

