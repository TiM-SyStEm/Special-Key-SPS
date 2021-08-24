using System;
using System.Windows.Forms;

namespace Special_KeyCoder
{
    public partial class allmodules : Form
    {
        public allmodules()
        {
            InitializeComponent();
            listBox1.Items.AddRange(Form1.modules.ToArray());
        }

        private void button1_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void listBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            string selected = listBox1.SelectedItem.ToString();
            Form1.text = "Add " + selected + "\n" + Form1.text;
        }
    }
}
