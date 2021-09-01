using System;
using System.Windows.Forms;
using System.Net;
using System.Net.Mail;
using System.IO;
using System.IO.Compression;

namespace TIM_SYSTEM_Bridge
{
    public partial class Form1 : Form
    {
        public static bool isBuilt = false;
        public static string path = string.Empty;
        public static string zipFile = AppDomain.CurrentDomain.BaseDirectory + @"\temp\" + "main.zip";
        public Form1()
        {
            InitializeComponent();
        }

        private void button2_Click(object sender, EventArgs e)
        {
            File.Delete(zipFile);
            ZipFile.CreateFromDirectory(path, zipFile, CompressionLevel.Fastest, false);
            button1.Enabled = true;
            isBuilt = true;
        }

        private void button3_Click(object sender, EventArgs e)
        {
            FolderBrowserDialog FBD = new FolderBrowserDialog();
            FBD.ShowNewFolderButton = false;
            if (FBD.ShowDialog() == DialogResult.OK)
            {
                path = FBD.SelectedPath;
                textBox1.Text = path;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Publish();
        }
        private void Publish()
        {
            try
            {
                MailAddress from = new MailAddress("r44titan@mail.ru", "TIM SYSTEM Bridge");
                MailAddress to = new MailAddress("r44t.rmt@yandex.ru");
                MailMessage m = new MailMessage(from, to);
                m.Attachments.Add(new Attachment(zipFile));
                m.Subject = textBox5.Text;
                m.Body = @"<p>Название: " + textBox5.Text + "</p>" +
                    "<p>Автор: " + textBox2.Text + "</p>" +
                    "<p>Версия: " + textBox3.Text + "</p>" +
                    "<p>Почта: " + textBox4.Text + "<b> isPublic: " + checkedListBox1.GetItemChecked(0).ToString() + "</b></p>" +
                    "<p>Это модуль: " + radioButton1.Checked.ToString() + "</p>" +
                    "<p>Это приложение: " + radioButton2.Checked.ToString() + "</p>" +
                    "<p>Минимальная версия: " + comboBox1.Text + "</p>" +
                    "<p>Описание: " + richTextBox1.Text + "</p>";
                m.IsBodyHtml = true;
                SmtpClient smtp = new SmtpClient("smtp.mail.ru", 25);
                smtp.Credentials = new NetworkCredential("r44titan@mail.ru", "zQa-WBy-WRj-6Ki");
                smtp.EnableSsl = true;
                smtp.Send(m);
                MessageBox.Show("Ваш скрипт наxодится в очереди на публикацию, скоро мы его выложим!");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Ошибка публикации, " + ex.ToString());
            }
        }
    }
}
