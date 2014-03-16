using System;
using System.IO;
using System.Drawing;
using System.Windows.Forms;
using System.Collections.Generic;

using ICSharpCode.SharpZipLib.Zip;


namespace DiamondSwordDev.Moddle2
{
	public partial class MainForm : Form
	{
		public MainForm()
		{
			InitializeComponent();
		}
		
		
		
		List<string> Usernames = new List<string>();
		List<string> Passwords = new List<string>();
		
		
		
		void MainFormLoad(object sender, EventArgs e)
		{
			if (!Directory.Exists(AppDomain.CurrentDomain.BaseDirectory + "packs\\"))
				Directory.CreateDirectory(AppDomain.CurrentDomain.BaseDirectory + "packs\\");
			
			if (!Directory.Exists(AppDomain.CurrentDomain.BaseDirectory + "launcher\\"))
				Directory.CreateDirectory(AppDomain.CurrentDomain.BaseDirectory + "launcher\\");
			
			if (!Directory.Exists(AppDomain.CurrentDomain.BaseDirectory + "launcher\\images\\"))
				Directory.CreateDirectory(AppDomain.CurrentDomain.BaseDirectory + "launcher\\images\\");
			
			foreach (string modpackZipfile in Directory.GetFiles(AppDomain.CurrentDomain.BaseDirectory + "packs\\", "*.zip"))
			{
				FileInfo packFileInfo = new FileInfo(modpackZipfile);
				
				ZipFile packZip = new ZipFile(modpackZipfile);
				ZipEntry imageEntry = new ZipEntry(packFileInfo.Name.Replace(".zip", ".png"));
				
				//if (imageEntry != null)
				//{
					Stream imageFileStream = packZip.GetInputStream(imageEntry);
					byte[] buffer = new byte[imageEntry.Size];
					imageFileStream.Read(buffer, 0, buffer.Length);
					File.WriteAllBytes(AppDomain.CurrentDomain.BaseDirectory + "launcher\\images\\" + packFileInfo.Name.Replace(".zip", ".png"), buffer);
				//}
				
				ModpackPictureBox modpackBox = new ModpackPictureBox();
				modpackBox.Location = new Point(5, (ModpackPanel.Controls.Count - 1) * 101);
				modpackBox.ModpackName = packFileInfo.Name.Replace(".zip", "");
				modpackBox.Image = new Bitmap(AppDomain.CurrentDomain.BaseDirectory + "launcher\\images\\" + packFileInfo.Name.Replace(".zip", ".png"));
				modpackBox.Click += new EventHandler(ModpackBoxClicked);
				
				ModpackPanel.Controls.Add(modpackBox);
				
				if (ModpackPanel.Controls.Count > 4)
					ModpackVerticalScrollBar.Maximum = (ModpackPanel.Controls.Count - 4) * 101;
				else
					ModpackVerticalScrollBar.Maximum = 0;
			}
		}
		
		void ModpackVerticalScrollBarScroll(object sender, ScrollEventArgs e)
		{
			int index = 0;
			
			foreach (Control ctrl in ModpackPanel.Controls)
			{
				if (ctrl is ModpackPictureBox)
				{
					ctrl.Location = new Point(5, (index * 101) - ModpackVerticalScrollBar.Value);
					index++;
				}
			}
		}
		
		void ModpackBoxClicked(object sender, EventArgs e)
		{
			foreach (Control ctrl in ModpackPanel.Controls)
				if (ctrl is ModpackPictureBox)
					(ctrl as ModpackPictureBox).BorderStyle = BorderStyle.None;
			
			(sender as ModpackPictureBox).BorderStyle = BorderStyle.FixedSingle;
		}
	}
}
