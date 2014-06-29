/*
 * Created by SharpDevelop.
 * User: LukeSmalley
 * Date: 1/30/2014
 * Time: 11:08 AM
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

namespace ModdleFrontend2
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
		
		List<string> Modpacks = new List<string>();
		List<string> Descriptions = new List<string>();
		
		void MainFormLoad(object sender, EventArgs e)
		{
			try
			{
				if (File.Exists(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat"))
				{
					UsernameBox.Text = File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat")[0];
					PasswordBox.Text = File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat")[1];
				}
				
				foreach (string f in Directory.GetFiles(AppDomain.CurrentDomain.BaseDirectory + "packs\\", "*.zip"))
				{
					FileInfo fi = new FileInfo(f);
					
					FastZip fz = new FastZip();
					fz.ExtractZip(f, AppDomain.CurrentDomain.BaseDirectory + "etmp\\", null);
					
					Modpacks.Add(fi.Name.Replace(".zip", ""));
					Descriptions.Add(File.ReadAllText(AppDomain.CurrentDomain.BaseDirectory + "etmp\\desc.txt"));
					
					ModListBox.Items.Add(fi.Name.Replace(".zip", ""));
				}
				
				ModListBox.SelectedIndex = 0;
				
				if (File.Exists(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat"))
				{
					string selectedItem = File.ReadAllLines(AppDomain.CurrentDomain.BaseDirectory + "lastlogin.dat")[2];
					foreach (string item in ModListBox.Items)
						if (item == selectedItem)
							ModListBox.SelectedItem = item;
				}
				
				ModNameLabel.Text = Modpacks.ToArray()[ModListBox.SelectedIndices[0]];
				ModDescriptionBox.Text = Descriptions.ToArray()[ModListBox.SelectedIndices[0]];
				
				Directory.Delete(AppDomain.CurrentDomain.BaseDirectory + "etmp\\", true);
			}
			catch (Exception ex)
			{
				ErrorDialog err = new ErrorDialog();
				err.ErrorTextBox.Text = ex.Message + "\r\n\r\nSource:  " + ex.Source + "\r\n\r\n" + ex.StackTrace;
				err.ShowDialog();
			}
		}
		
		
		
		void ModListBoxSelectedIndexChanged(object sender, EventArgs e)
		{
			try
			{
				ModNameLabel.Text = Modpacks.ToArray()[ModListBox.SelectedIndices[0]];
				ModDescriptionBox.Text = Descriptions.ToArray()[ModListBox.SelectedIndices[0]];
			}
			catch (Exception ex)
			{
				ErrorDialog err = new ErrorDialog();
				err.ErrorTextBox.Text = ex.Message + "\r\n\r\nSource:  " + ex.Source + "\r\n\r\n" + ex.StackTrace;
				err.ShowDialog();
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
				ErrorDialog err = new ErrorDialog();
				err.ErrorTextBox.Text = ex.Message + "\r\n\r\nSource:  " + ex.Source + "\r\n\r\n" + ex.StackTrace;
				err.ShowDialog();
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
				ErrorDialog err = new ErrorDialog();
				err.ErrorTextBox.Text = ex.Message + "\r\n\r\nSource:  " + ex.Source + "\r\n\r\n" + ex.StackTrace;
				err.ShowDialog();
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
				ErrorDialog err = new ErrorDialog();
				err.ErrorTextBox.Text = ex.Message + "\r\n\r\nSource:  " + ex.Source + "\r\n\r\n" + ex.StackTrace;
				err.ShowDialog();
			}
		}
	}
}
