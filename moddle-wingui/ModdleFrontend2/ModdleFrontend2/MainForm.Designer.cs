/*
 * Created by SharpDevelop.
 * User: LukeSmalley
 * Date: 1/30/2014
 * Time: 11:08 AM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
namespace ModdleFrontend2
{
	partial class MainForm
	{
		/// <summary>
		/// Designer variable used to keep track of non-visual components.
		/// </summary>
		private System.ComponentModel.IContainer components = null;
		
		/// <summary>
		/// Disposes resources used by the form.
		/// </summary>
		/// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
		protected override void Dispose(bool disposing)
		{
			if (disposing) {
				if (components != null) {
					components.Dispose();
				}
			}
			base.Dispose(disposing);
		}
		
		/// <summary>
		/// This method is required for Windows Forms designer support.
		/// Do not change the method contents inside the source code editor. The Forms designer might
		/// not be able to load this method if it was changed manually.
		/// </summary>
		private void InitializeComponent()
		{
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
			this.InstanceAssetTabs = new System.Windows.Forms.TabControl();
			this.ModpacksTabPage = new System.Windows.Forms.TabPage();
			this.ModListBox = new System.Windows.Forms.ListBox();
			this.ModNameLabel = new System.Windows.Forms.Label();
			this.ModDescriptionBox = new System.Windows.Forms.RichTextBox();
			this.MapsTabPage = new System.Windows.Forms.TabPage();
			this.ResourcePacksTabPage = new System.Windows.Forms.TabPage();
			this.CleanCheckBox = new System.Windows.Forms.CheckBox();
			this.ServerButton = new System.Windows.Forms.Button();
			this.ClearLabel = new System.Windows.Forms.Label();
			this.ForceUpdateCheckBox = new System.Windows.Forms.CheckBox();
			this.PlayButton = new System.Windows.Forms.Button();
			this.PasswordLabel = new System.Windows.Forms.Label();
			this.PasswordBox = new System.Windows.Forms.TextBox();
			this.UsernameLabel = new System.Windows.Forms.Label();
			this.UsernameBox = new System.Windows.Forms.TextBox();
			this.MapListBox = new System.Windows.Forms.ListBox();
			this.MapNameLabel = new System.Windows.Forms.Label();
			this.MapDescriptionBox = new System.Windows.Forms.RichTextBox();
			this.InstanceAssetTabs.SuspendLayout();
			this.ModpacksTabPage.SuspendLayout();
			this.MapsTabPage.SuspendLayout();
			this.SuspendLayout();
			// 
			// InstanceAssetTabs
			// 
			this.InstanceAssetTabs.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.InstanceAssetTabs.Controls.Add(this.ModpacksTabPage);
			this.InstanceAssetTabs.Controls.Add(this.MapsTabPage);
			this.InstanceAssetTabs.Controls.Add(this.ResourcePacksTabPage);
			this.InstanceAssetTabs.Location = new System.Drawing.Point(7, 5);
			this.InstanceAssetTabs.Name = "InstanceAssetTabs";
			this.InstanceAssetTabs.SelectedIndex = 0;
			this.InstanceAssetTabs.Size = new System.Drawing.Size(775, 353);
			this.InstanceAssetTabs.TabIndex = 22;
			// 
			// ModpacksTabPage
			// 
			this.ModpacksTabPage.BackColor = System.Drawing.Color.Gainsboro;
			this.ModpacksTabPage.Controls.Add(this.ModListBox);
			this.ModpacksTabPage.Controls.Add(this.ModNameLabel);
			this.ModpacksTabPage.Controls.Add(this.ModDescriptionBox);
			this.ModpacksTabPage.Location = new System.Drawing.Point(4, 22);
			this.ModpacksTabPage.Name = "ModpacksTabPage";
			this.ModpacksTabPage.Padding = new System.Windows.Forms.Padding(3);
			this.ModpacksTabPage.Size = new System.Drawing.Size(767, 327);
			this.ModpacksTabPage.TabIndex = 0;
			this.ModpacksTabPage.Text = "Modpacks";
			// 
			// ModListBox
			// 
			this.ModListBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left)));
			this.ModListBox.BackColor = System.Drawing.Color.WhiteSmoke;
			this.ModListBox.Font = new System.Drawing.Font("Tahoma", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ModListBox.FormattingEnabled = true;
			this.ModListBox.ItemHeight = 18;
			this.ModListBox.Location = new System.Drawing.Point(3, 6);
			this.ModListBox.Name = "ModListBox";
			this.ModListBox.Size = new System.Drawing.Size(179, 310);
			this.ModListBox.TabIndex = 9;
			this.ModListBox.SelectedIndexChanged += new System.EventHandler(this.ModListBoxSelectedIndexChanged);
			// 
			// ModNameLabel
			// 
			this.ModNameLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.ModNameLabel.BackColor = System.Drawing.Color.Transparent;
			this.ModNameLabel.Font = new System.Drawing.Font("Tahoma", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ModNameLabel.Location = new System.Drawing.Point(188, 3);
			this.ModNameLabel.Name = "ModNameLabel";
			this.ModNameLabel.Size = new System.Drawing.Size(576, 23);
			this.ModNameLabel.TabIndex = 1;
			this.ModNameLabel.Text = "No packs are selected";
			// 
			// ModDescriptionBox
			// 
			this.ModDescriptionBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.ModDescriptionBox.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
			this.ModDescriptionBox.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
			this.ModDescriptionBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ModDescriptionBox.Location = new System.Drawing.Point(188, 29);
			this.ModDescriptionBox.Name = "ModDescriptionBox";
			this.ModDescriptionBox.ReadOnly = true;
			this.ModDescriptionBox.ScrollBars = System.Windows.Forms.RichTextBoxScrollBars.Vertical;
			this.ModDescriptionBox.Size = new System.Drawing.Size(579, 297);
			this.ModDescriptionBox.TabIndex = 8;
			this.ModDescriptionBox.Text = "";
			this.ModDescriptionBox.LinkClicked += new System.Windows.Forms.LinkClickedEventHandler(this.ModDescriptionBoxLinkClicked);
			// 
			// MapsTabPage
			// 
			this.MapsTabPage.BackColor = System.Drawing.Color.Gainsboro;
			this.MapsTabPage.Controls.Add(this.MapListBox);
			this.MapsTabPage.Controls.Add(this.MapNameLabel);
			this.MapsTabPage.Controls.Add(this.MapDescriptionBox);
			this.MapsTabPage.Location = new System.Drawing.Point(4, 22);
			this.MapsTabPage.Name = "MapsTabPage";
			this.MapsTabPage.Padding = new System.Windows.Forms.Padding(3);
			this.MapsTabPage.Size = new System.Drawing.Size(767, 327);
			this.MapsTabPage.TabIndex = 1;
			this.MapsTabPage.Text = "Maps";
			// 
			// ResourcePacksTabPage
			// 
			this.ResourcePacksTabPage.BackColor = System.Drawing.Color.Gainsboro;
			this.ResourcePacksTabPage.Location = new System.Drawing.Point(4, 22);
			this.ResourcePacksTabPage.Name = "ResourcePacksTabPage";
			this.ResourcePacksTabPage.Padding = new System.Windows.Forms.Padding(3);
			this.ResourcePacksTabPage.Size = new System.Drawing.Size(767, 327);
			this.ResourcePacksTabPage.TabIndex = 2;
			this.ResourcePacksTabPage.Text = "Resource Packs";
			// 
			// CleanCheckBox
			// 
			this.CleanCheckBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.CleanCheckBox.BackColor = System.Drawing.Color.Transparent;
			this.CleanCheckBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.CleanCheckBox.ForeColor = System.Drawing.Color.Navy;
			this.CleanCheckBox.Location = new System.Drawing.Point(376, 392);
			this.CleanCheckBox.Name = "CleanCheckBox";
			this.CleanCheckBox.Size = new System.Drawing.Size(56, 24);
			this.CleanCheckBox.TabIndex = 18;
			this.CleanCheckBox.Text = "Clean";
			this.CleanCheckBox.UseVisualStyleBackColor = false;
			this.CleanCheckBox.Visible = false;
			this.CleanCheckBox.CheckedChanged += new System.EventHandler(this.CleanCheckBoxCheckedChanged);
			// 
			// ServerButton
			// 
			this.ServerButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.ServerButton.Font = new System.Drawing.Font("Tahoma", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ServerButton.Location = new System.Drawing.Point(722, 364);
			this.ServerButton.Name = "ServerButton";
			this.ServerButton.Size = new System.Drawing.Size(60, 51);
			this.ServerButton.TabIndex = 19;
			this.ServerButton.Text = "Run Server";
			this.ServerButton.UseVisualStyleBackColor = true;
			// 
			// ClearLabel
			// 
			this.ClearLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.ClearLabel.BackColor = System.Drawing.Color.Transparent;
			this.ClearLabel.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ClearLabel.ForeColor = System.Drawing.Color.Red;
			this.ClearLabel.Location = new System.Drawing.Point(276, 356);
			this.ClearLabel.Name = "ClearLabel";
			this.ClearLabel.Size = new System.Drawing.Size(156, 39);
			this.ClearLabel.TabIndex = 21;
			this.ClearLabel.Text = "This will clear all your game\r\ndata and all of your saves!";
			this.ClearLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.ClearLabel.Visible = false;
			// 
			// ForceUpdateCheckBox
			// 
			this.ForceUpdateCheckBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.ForceUpdateCheckBox.BackColor = System.Drawing.Color.Transparent;
			this.ForceUpdateCheckBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ForceUpdateCheckBox.Location = new System.Drawing.Point(276, 392);
			this.ForceUpdateCheckBox.Name = "ForceUpdateCheckBox";
			this.ForceUpdateCheckBox.Size = new System.Drawing.Size(104, 24);
			this.ForceUpdateCheckBox.TabIndex = 17;
			this.ForceUpdateCheckBox.Text = "Force Update";
			this.ForceUpdateCheckBox.UseVisualStyleBackColor = false;
			this.ForceUpdateCheckBox.CheckedChanged += new System.EventHandler(this.ForceUpdateCheckBoxCheckedChanged);
			// 
			// PlayButton
			// 
			this.PlayButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.PlayButton.Font = new System.Drawing.Font("Tahoma", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.PlayButton.Location = new System.Drawing.Point(549, 364);
			this.PlayButton.Name = "PlayButton";
			this.PlayButton.Size = new System.Drawing.Size(167, 51);
			this.PlayButton.TabIndex = 15;
			this.PlayButton.Text = "Play";
			this.PlayButton.UseVisualStyleBackColor = true;
			// 
			// PasswordLabel
			// 
			this.PasswordLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.PasswordLabel.BackColor = System.Drawing.Color.Transparent;
			this.PasswordLabel.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.PasswordLabel.Location = new System.Drawing.Point(7, 392);
			this.PasswordLabel.Name = "PasswordLabel";
			this.PasswordLabel.Size = new System.Drawing.Size(66, 22);
			this.PasswordLabel.TabIndex = 20;
			this.PasswordLabel.Text = "Password:";
			this.PasswordLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			// 
			// PasswordBox
			// 
			this.PasswordBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.PasswordBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.PasswordBox.Location = new System.Drawing.Point(79, 392);
			this.PasswordBox.Name = "PasswordBox";
			this.PasswordBox.PasswordChar = '●';
			this.PasswordBox.Size = new System.Drawing.Size(191, 22);
			this.PasswordBox.TabIndex = 14;
			// 
			// UsernameLabel
			// 
			this.UsernameLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.UsernameLabel.BackColor = System.Drawing.Color.Transparent;
			this.UsernameLabel.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.UsernameLabel.Location = new System.Drawing.Point(7, 364);
			this.UsernameLabel.Name = "UsernameLabel";
			this.UsernameLabel.Size = new System.Drawing.Size(66, 22);
			this.UsernameLabel.TabIndex = 16;
			this.UsernameLabel.Text = "Username:";
			this.UsernameLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			// 
			// UsernameBox
			// 
			this.UsernameBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.UsernameBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.UsernameBox.Location = new System.Drawing.Point(79, 364);
			this.UsernameBox.Name = "UsernameBox";
			this.UsernameBox.Size = new System.Drawing.Size(191, 22);
			this.UsernameBox.TabIndex = 13;
			// 
			// MapListBox
			// 
			this.MapListBox.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left)));
			this.MapListBox.BackColor = System.Drawing.Color.WhiteSmoke;
			this.MapListBox.Font = new System.Drawing.Font("Tahoma", 11.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.MapListBox.FormattingEnabled = true;
			this.MapListBox.ItemHeight = 18;
			this.MapListBox.Location = new System.Drawing.Point(3, 6);
			this.MapListBox.Name = "MapListBox";
			this.MapListBox.Size = new System.Drawing.Size(179, 310);
			this.MapListBox.TabIndex = 12;
			// 
			// MapNameLabel
			// 
			this.MapNameLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.MapNameLabel.BackColor = System.Drawing.Color.Transparent;
			this.MapNameLabel.Font = new System.Drawing.Font("Tahoma", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.MapNameLabel.Location = new System.Drawing.Point(188, 3);
			this.MapNameLabel.Name = "MapNameLabel";
			this.MapNameLabel.Size = new System.Drawing.Size(576, 23);
			this.MapNameLabel.TabIndex = 10;
			this.MapNameLabel.Text = "No maps are selected";
			// 
			// MapDescriptionBox
			// 
			this.MapDescriptionBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.MapDescriptionBox.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
			this.MapDescriptionBox.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
			this.MapDescriptionBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.MapDescriptionBox.Location = new System.Drawing.Point(188, 29);
			this.MapDescriptionBox.Name = "MapDescriptionBox";
			this.MapDescriptionBox.ReadOnly = true;
			this.MapDescriptionBox.ScrollBars = System.Windows.Forms.RichTextBoxScrollBars.Vertical;
			this.MapDescriptionBox.Size = new System.Drawing.Size(579, 297);
			this.MapDescriptionBox.TabIndex = 11;
			this.MapDescriptionBox.Text = "";
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
			this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
			this.ClientSize = new System.Drawing.Size(789, 421);
			this.Controls.Add(this.InstanceAssetTabs);
			this.Controls.Add(this.CleanCheckBox);
			this.Controls.Add(this.ServerButton);
			this.Controls.Add(this.ClearLabel);
			this.Controls.Add(this.ForceUpdateCheckBox);
			this.Controls.Add(this.PlayButton);
			this.Controls.Add(this.PasswordLabel);
			this.Controls.Add(this.PasswordBox);
			this.Controls.Add(this.UsernameLabel);
			this.Controls.Add(this.UsernameBox);
			this.DoubleBuffered = true;
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.MaximizeBox = false;
			this.MinimumSize = new System.Drawing.Size(750, 400);
			this.Name = "MainForm";
			this.Text = "Moddle Indev";
			this.Load += new System.EventHandler(this.MainFormLoad);
			this.InstanceAssetTabs.ResumeLayout(false);
			this.ModpacksTabPage.ResumeLayout(false);
			this.MapsTabPage.ResumeLayout(false);
			this.ResumeLayout(false);
			this.PerformLayout();
		}
		private System.Windows.Forms.RichTextBox MapDescriptionBox;
		private System.Windows.Forms.Label MapNameLabel;
		private System.Windows.Forms.ListBox MapListBox;
		private System.Windows.Forms.ListBox ModListBox;
		private System.Windows.Forms.TextBox UsernameBox;
		private System.Windows.Forms.Label UsernameLabel;
		private System.Windows.Forms.TextBox PasswordBox;
		private System.Windows.Forms.Label PasswordLabel;
		private System.Windows.Forms.Button PlayButton;
		private System.Windows.Forms.CheckBox ForceUpdateCheckBox;
		private System.Windows.Forms.Label ClearLabel;
		private System.Windows.Forms.Button ServerButton;
		private System.Windows.Forms.CheckBox CleanCheckBox;
		private System.Windows.Forms.TabPage ResourcePacksTabPage;
		private System.Windows.Forms.TabPage MapsTabPage;
		private System.Windows.Forms.RichTextBox ModDescriptionBox;
		private System.Windows.Forms.Label ModNameLabel;
		private System.Windows.Forms.TabPage ModpacksTabPage;
		private System.Windows.Forms.TabControl InstanceAssetTabs;
	}
}
