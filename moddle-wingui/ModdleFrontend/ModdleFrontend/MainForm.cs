/*
 * Created by SharpDevelop.
 * User: LukeSmalley
 * Date: 12/7/2013
 * Time: 9:09 PM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Windows.Forms;
using System.IO;
using System.Diagnostics;

using ICSharpCode.SharpZipLib.Zip;

namespace ModdleFrontend
{
	/// <summary>
	/// Description of MainForm.
	/// </summary>
	public partial class MainForm : Form
	{
		public MainForm()
		{
			//
			// The InitializeComponent() call is required for Windows Forms designer support.
			//
			InitializeComponent();
			
			//
			// TODO: Add constructor code after the InitializeComponent() call.
			//
		}
		
		List<string> packs = new List<string>();
		List<string> desc = new List<string>();
		List<Image> icons = new List<Image>();
		
		void MainFormLoad(object sender, EventArgs e)
		{
			try
			{
				if (File.Exists(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat"))
				{
					//File.Decrypt(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat");
					UsernameBox.Text = File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat")[0];
					PasswordBox.Text = File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat")[1];
					//File.Encrypt(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat");
				}
				
				//ModListBox.SmallImageList = ModListImages;
				
				if (!Directory.Exists(AppDomain.CurrentDomain.BaseDirectory + "mfrontend\\"))
					Directory.CreateDirectory(AppDomain.CurrentDomain.BaseDirectory + "mfrontend\\");
				
				foreach (string f in Directory.GetFiles(AppDomain.CurrentDomain.BaseDirectory + "packs\\", "*.zip"))
				{
					FileInfo fi = new FileInfo(f);
					
					FastZip fz = new FastZip();
					fz.ExtractZip(f, AppDomain.CurrentDomain.BaseDirectory + "etmp\\", null);
					
					packs.Add(fi.Name.Replace(".zip", ""));
					desc.Add(File.ReadAllText(AppDomain.CurrentDomain.BaseDirectory + "etmp\\desc.txt"));
					//icons.Add(new Bitmap(AppDomain.CurrentDomain.BaseDirectory + "etmp\\pack.png"));
					
					File.Copy(AppDomain.CurrentDomain.BaseDirectory + "etmp\\pack.png", AppDomain.CurrentDomain.BaseDirectory + "mfrontend\\" + fi.Name.Replace(".zip", "") + ".png", true);
					ModListImages.Images.Add(new Bitmap(AppDomain.CurrentDomain.BaseDirectory + "mfrontend\\" + fi.Name.Replace(".zip", "") + ".png"));
					
					ListViewItem item = new ListViewItem();
					item.Text = fi.Name.Replace(".zip", "");
					item.Font = new Font("Trebuchet", 11f, FontStyle.Bold);
					item.ImageIndex = ModListImages.Images.Count - 1;
					
					ModListBox.Items.Add(item);
				}
				
				ModListBox.SelectedIndices.Add(0);
				
				if (File.Exists(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat"))
				{
					string selectedItem = File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat")[2];
					foreach (ListViewItem item in ModListBox.Items)
						if (item.Text == selectedItem)
							item.Selected = true;
				}
				
				ModNameLabel.Text = packs.ToArray()[ModListBox.SelectedIndices[0]];
				ModDescriptionBox.Text = desc.ToArray()[ModListBox.SelectedIndices[0]];
				
				Directory.Delete(AppDomain.CurrentDomain.BaseDirectory + "etmp\\", true);
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
		
		void ModListBoxSelectedIndexChanged(object sender, EventArgs e)
		{
			try
			{
				ModNameLabel.Text = packs.ToArray()[ModListBox.SelectedIndices[0]];
				ModDescriptionBox.Text = desc.ToArray()[ModListBox.SelectedIndices[0]];
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
		
		void PlayButtonClick(object sender, EventArgs e)
		{
			try
			{
				//string contents = File.ReadAllText(AppDomain.CurrentDomain.BaseDirectory + "start.template.bat");
				//contents = contents.Replace("{USERNAME}", UsernameBox.Text).Replace("{PASSWORD}", PasswordBox.Text).Replace("{MODPACK}", ModListBox.SelectedItem.ToString()).Replace("{FORCEUPDATE}", ForceUpdateCheckBox.Checked.ToString());
				//File.WriteAllText(AppDomain.CurrentDomain.BaseDirectory + "start.bat", contents);
				//Process.Start(AppDomain.CurrentDomain.BaseDirectory + "start.bat");
				Process moddle = new Process();
				moddle.StartInfo.FileName = AppDomain.CurrentDomain.BaseDirectory + "moddle.py";
				if (File.Exists(AppDomain.CurrentDomain.BaseDirectory + "launchargs.dat"))
				{
					moddle.StartInfo.Arguments = File.ReadAllText(AppDomain.CurrentDomain.BaseDirectory + "launchargs.dat").Replace("{USERNAME}", UsernameBox.Text).Replace("{PASSWORD}", PasswordBox.Text).Replace("{MODPACK}", ModListBox.SelectedItems[0].Text).Replace("{FORCEUPDATE}", ForceUpdateCheckBox.Checked.ToString().Replace("{CLEAN}", (CleanCheckBox.Checked & ForceUpdateCheckBox.Checked).ToString())); //"-nolaunch -modpack=" + ModListBox.SelectedItem.ToString() + " -username=" + UsernameBox.Text + " -password=" + PasswordBox.Text;
				}
				else
				{
					moddle.StartInfo.Arguments = "-nogui -modpack=" + ModListBox.SelectedItems[0].Text + " -username=" + UsernameBox.Text + " -password=" + PasswordBox.Text + " -forceupdate=" + ForceUpdateCheckBox.Checked.ToString() + " -clean=" + (CleanCheckBox.Checked & ForceUpdateCheckBox.Checked).ToString();
				}
				moddle.Start();
				
				File.WriteAllText(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat", UsernameBox.Text + "\n" + PasswordBox.Text + "\n" + ModListBox.SelectedItems[0].Text);
				
				Application.Exit();
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
		
		void ForceUpdateCheckBoxCheckedChanged(object sender, EventArgs e)
		{
			try
			{
				if (ForceUpdateCheckBox.Checked)
				{
					CleanCheckBox.Visible = true;
					ClearLabel.Visible = CleanCheckBox.Checked;
				}
				else
				{
					CleanCheckBox.Visible = false;
					ClearLabel.Visible = false;
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
		
		void ModDescriptionBoxLinkClicked(object sender, LinkClickedEventArgs e)
		{
			try
			{
				Process.Start(e.LinkText);
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
		
		void ServerButtonClick(object sender, EventArgs e)
		{
			try
			{
				Process moddle = new Process();
				moddle.StartInfo.FileName = AppDomain.CurrentDomain.BaseDirectory + "moddle.py";
				if (File.Exists(AppDomain.CurrentDomain.BaseDirectory + "launchargs_server.dat"))
				{
					moddle.StartInfo.Arguments = File.ReadAllText(AppDomain.CurrentDomain.BaseDirectory + "launchargs_server.dat").Replace("{USERNAME}", UsernameBox.Text).Replace("{PASSWORD}", PasswordBox.Text).Replace("{MODPACK}", ModListBox.SelectedItems[0].Text).Replace("{FORCEUPDATE}", ForceUpdateCheckBox.Checked.ToString()); //"-nolaunch -modpack=" + ModListBox.SelectedItem.ToString() + " -username=" + UsernameBox.Text + " -password=" + PasswordBox.Text;
				}
				else
				{
					moddle.StartInfo.Arguments = "-nogui -runserver -modpack=" + ModListBox.SelectedItems[0].Text + " -forceupdate=" + ForceUpdateCheckBox.Checked.ToString();
				}
				moddle.Start();
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
		
		void CleanCheckBoxCheckedChanged(object sender, EventArgs e)
		{
			try
			{
				if (CleanCheckBox.Checked)
				{
					ClearLabel.Visible = true;
				}
				else
				{
					ClearLabel.Visible = false;
				}
			}
			catch (Exception ex)
			{
				MessageBox.Show("O_O An Error Occured.\n\nStability is highly overrated.\n\n(" + ex.Message + ")");
			}
		}
	}
}
