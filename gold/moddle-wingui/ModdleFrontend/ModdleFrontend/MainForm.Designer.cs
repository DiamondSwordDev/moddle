/*
 * Created by SharpDevelop.
 * User: LukeSmalley
 * Date: 12/7/2013
 * Time: 9:09 PM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
namespace ModdleFrontend
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
			this.components = new System.ComponentModel.Container();
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainForm));
			this.ModNameLabel = new System.Windows.Forms.Label();
			this.UsernameBox = new System.Windows.Forms.TextBox();
			this.label2 = new System.Windows.Forms.Label();
			this.label3 = new System.Windows.Forms.Label();
			this.PasswordBox = new System.Windows.Forms.TextBox();
			this.PlayButton = new System.Windows.Forms.Button();
			this.ForceUpdateCheckBox = new System.Windows.Forms.CheckBox();
			this.ClearLabel = new System.Windows.Forms.Label();
			this.ModDescriptionBox = new System.Windows.Forms.RichTextBox();
			this.ServerButton = new System.Windows.Forms.Button();
			this.CleanCheckBox = new System.Windows.Forms.CheckBox();
			this.ModListBox = new System.Windows.Forms.ListView();
			this.ModListImages = new System.Windows.Forms.ImageList(this.components);
			this.SuspendLayout();
			// 
			// ModNameLabel
			// 
			this.ModNameLabel.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.ModNameLabel.BackColor = System.Drawing.Color.Transparent;
			this.ModNameLabel.Font = new System.Drawing.Font("Tahoma", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ModNameLabel.Location = new System.Drawing.Point(200, 12);
			this.ModNameLabel.Name = "ModNameLabel";
			this.ModNameLabel.Size = new System.Drawing.Size(588, 23);
			this.ModNameLabel.TabIndex = 1;
			this.ModNameLabel.Text = "No packs are selected";
			// 
			// UsernameBox
			// 
			this.UsernameBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.UsernameBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.UsernameBox.Location = new System.Drawing.Point(84, 362);
			this.UsernameBox.Name = "UsernameBox";
			this.UsernameBox.Size = new System.Drawing.Size(191, 22);
			this.UsernameBox.TabIndex = 1;
			// 
			// label2
			// 
			this.label2.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.label2.BackColor = System.Drawing.Color.Transparent;
			this.label2.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.label2.Location = new System.Drawing.Point(12, 362);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(66, 22);
			this.label2.TabIndex = 4;
			this.label2.Text = "Username:";
			this.label2.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			// 
			// label3
			// 
			this.label3.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.label3.BackColor = System.Drawing.Color.Transparent;
			this.label3.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.label3.Location = new System.Drawing.Point(12, 390);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(66, 22);
			this.label3.TabIndex = 6;
			this.label3.Text = "Password:";
			this.label3.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			// 
			// PasswordBox
			// 
			this.PasswordBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.PasswordBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.PasswordBox.Location = new System.Drawing.Point(84, 390);
			this.PasswordBox.Name = "PasswordBox";
			this.PasswordBox.PasswordChar = '●';
			this.PasswordBox.Size = new System.Drawing.Size(191, 22);
			this.PasswordBox.TabIndex = 2;
			// 
			// PlayButton
			// 
			this.PlayButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.PlayButton.Font = new System.Drawing.Font("Tahoma", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.PlayButton.Location = new System.Drawing.Point(506, 362);
			this.PlayButton.Name = "PlayButton";
			this.PlayButton.Size = new System.Drawing.Size(216, 51);
			this.PlayButton.TabIndex = 3;
			this.PlayButton.Text = "Play";
			this.PlayButton.UseVisualStyleBackColor = true;
			this.PlayButton.Click += new System.EventHandler(this.PlayButtonClick);
			// 
			// ForceUpdateCheckBox
			// 
			this.ForceUpdateCheckBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.ForceUpdateCheckBox.BackColor = System.Drawing.Color.Transparent;
			this.ForceUpdateCheckBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ForceUpdateCheckBox.Location = new System.Drawing.Point(281, 390);
			this.ForceUpdateCheckBox.Name = "ForceUpdateCheckBox";
			this.ForceUpdateCheckBox.Size = new System.Drawing.Size(104, 24);
			this.ForceUpdateCheckBox.TabIndex = 4;
			this.ForceUpdateCheckBox.Text = "Force Update";
			this.ForceUpdateCheckBox.UseVisualStyleBackColor = false;
			this.ForceUpdateCheckBox.CheckedChanged += new System.EventHandler(this.ForceUpdateCheckBoxCheckedChanged);
			// 
			// ClearLabel
			// 
			this.ClearLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.ClearLabel.BackColor = System.Drawing.Color.Transparent;
			this.ClearLabel.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ClearLabel.ForeColor = System.Drawing.Color.Red;
			this.ClearLabel.Location = new System.Drawing.Point(281, 348);
			this.ClearLabel.Name = "ClearLabel";
			this.ClearLabel.Size = new System.Drawing.Size(156, 39);
			this.ClearLabel.TabIndex = 10;
			this.ClearLabel.Text = "This will clear all your game\r\ndata and all of your saves!";
			this.ClearLabel.TextAlign = System.Drawing.ContentAlignment.MiddleLeft;
			this.ClearLabel.Visible = false;
			// 
			// ModDescriptionBox
			// 
			this.ModDescriptionBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.ModDescriptionBox.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
			this.ModDescriptionBox.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
			this.ModDescriptionBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ModDescriptionBox.Location = new System.Drawing.Point(200, 38);
			this.ModDescriptionBox.Name = "ModDescriptionBox";
			this.ModDescriptionBox.ReadOnly = true;
			this.ModDescriptionBox.ScrollBars = System.Windows.Forms.RichTextBoxScrollBars.Vertical;
			this.ModDescriptionBox.Size = new System.Drawing.Size(588, 307);
			this.ModDescriptionBox.TabIndex = 8;
			this.ModDescriptionBox.Text = "";
			this.ModDescriptionBox.LinkClicked += new System.Windows.Forms.LinkClickedEventHandler(this.ModDescriptionBoxLinkClicked);
			// 
			// ServerButton
			// 
			this.ServerButton.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Right)));
			this.ServerButton.Font = new System.Drawing.Font("Tahoma", 9.75F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ServerButton.Location = new System.Drawing.Point(728, 362);
			this.ServerButton.Name = "ServerButton";
			this.ServerButton.Size = new System.Drawing.Size(60, 51);
			this.ServerButton.TabIndex = 6;
			this.ServerButton.Text = "Run Server";
			this.ServerButton.UseVisualStyleBackColor = true;
			this.ServerButton.Click += new System.EventHandler(this.ServerButtonClick);
			// 
			// CleanCheckBox
			// 
			this.CleanCheckBox.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)));
			this.CleanCheckBox.BackColor = System.Drawing.Color.Transparent;
			this.CleanCheckBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.CleanCheckBox.ForeColor = System.Drawing.Color.Navy;
			this.CleanCheckBox.Location = new System.Drawing.Point(381, 390);
			this.CleanCheckBox.Name = "CleanCheckBox";
			this.CleanCheckBox.Size = new System.Drawing.Size(56, 24);
			this.CleanCheckBox.TabIndex = 5;
			this.CleanCheckBox.Text = "Clean";
			this.CleanCheckBox.UseVisualStyleBackColor = false;
			this.CleanCheckBox.Visible = false;
			this.CleanCheckBox.CheckedChanged += new System.EventHandler(this.CleanCheckBoxCheckedChanged);
			// 
			// ModListBox
			// 
			this.ModListBox.BackColor = System.Drawing.Color.WhiteSmoke;
			this.ModListBox.LargeImageList = this.ModListImages;
			this.ModListBox.Location = new System.Drawing.Point(12, 12);
			this.ModListBox.MultiSelect = false;
			this.ModListBox.Name = "ModListBox";
			this.ModListBox.Size = new System.Drawing.Size(182, 333);
			this.ModListBox.SmallImageList = this.ModListImages;
			this.ModListBox.TabIndex = 11;
			this.ModListBox.UseCompatibleStateImageBehavior = false;
			this.ModListBox.View = System.Windows.Forms.View.SmallIcon;
			// 
			// ModListImages
			// 
			this.ModListImages.ColorDepth = System.Windows.Forms.ColorDepth.Depth32Bit;
			this.ModListImages.ImageSize = new System.Drawing.Size(64, 48);
			this.ModListImages.TransparentColor = System.Drawing.Color.Transparent;
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.BackColor = System.Drawing.Color.LightGray;
			this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
			this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Center;
			this.ClientSize = new System.Drawing.Size(800, 421);
			this.Controls.Add(this.ModListBox);
			this.Controls.Add(this.CleanCheckBox);
			this.Controls.Add(this.ServerButton);
			this.Controls.Add(this.ModDescriptionBox);
			this.Controls.Add(this.ClearLabel);
			this.Controls.Add(this.ForceUpdateCheckBox);
			this.Controls.Add(this.PlayButton);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.PasswordBox);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.UsernameBox);
			this.Controls.Add(this.ModNameLabel);
			this.DoubleBuffered = true;
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.MaximizeBox = false;
			this.MinimumSize = new System.Drawing.Size(750, 400);
			this.Name = "MainForm";
			this.Text = "Moddle Integrated Launcher";
			this.Load += new System.EventHandler(this.MainFormLoad);
			this.ResumeLayout(false);
			this.PerformLayout();
		}
		private System.Windows.Forms.ImageList ModListImages;
		private System.Windows.Forms.ListView ModListBox;
		private System.Windows.Forms.CheckBox CleanCheckBox;
		private System.Windows.Forms.Button ServerButton;
		private System.Windows.Forms.RichTextBox ModDescriptionBox;
		private System.Windows.Forms.Label ClearLabel;
		private System.Windows.Forms.CheckBox ForceUpdateCheckBox;
		private System.Windows.Forms.Button PlayButton;
		private System.Windows.Forms.TextBox PasswordBox;
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.TextBox UsernameBox;
		private System.Windows.Forms.Label ModNameLabel;
	}
}
