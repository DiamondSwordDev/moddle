/*
 * Created by SharpDevelop.
 * User: LukeSmalley
 * Date: 1/30/2014
 * Time: 11:36 AM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */
namespace ModdleFrontend2
{
	partial class ErrorDialog
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
			System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ErrorDialog));
			this.label1 = new System.Windows.Forms.Label();
			this.ErrorTextBox = new System.Windows.Forms.TextBox();
			this.OKButton = new System.Windows.Forms.Button();
			this.SuspendLayout();
			// 
			// label1
			// 
			this.label1.BackColor = System.Drawing.Color.Transparent;
			this.label1.Font = new System.Drawing.Font("Consolas", 14.25F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.label1.ForeColor = System.Drawing.Color.FromArgb(((int)(((byte)(192)))), ((int)(((byte)(0)))), ((int)(((byte)(0)))));
			this.label1.Location = new System.Drawing.Point(12, 9);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(362, 26);
			this.label1.TabIndex = 0;
			this.label1.Text = "An Error Has Occurred!";
			// 
			// ErrorTextBox
			// 
			this.ErrorTextBox.AcceptsReturn = true;
			this.ErrorTextBox.AcceptsTab = true;
			this.ErrorTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom) 
									| System.Windows.Forms.AnchorStyles.Left) 
									| System.Windows.Forms.AnchorStyles.Right)));
			this.ErrorTextBox.BackColor = System.Drawing.Color.WhiteSmoke;
			this.ErrorTextBox.Font = new System.Drawing.Font("Consolas", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.ErrorTextBox.Location = new System.Drawing.Point(12, 38);
			this.ErrorTextBox.Multiline = true;
			this.ErrorTextBox.Name = "ErrorTextBox";
			this.ErrorTextBox.ReadOnly = true;
			this.ErrorTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Both;
			this.ErrorTextBox.Size = new System.Drawing.Size(477, 183);
			this.ErrorTextBox.TabIndex = 1;
			this.ErrorTextBox.WordWrap = false;
			// 
			// OKButton
			// 
			this.OKButton.DialogResult = System.Windows.Forms.DialogResult.OK;
			this.OKButton.Location = new System.Drawing.Point(414, 227);
			this.OKButton.Name = "OKButton";
			this.OKButton.RightToLeft = System.Windows.Forms.RightToLeft.No;
			this.OKButton.Size = new System.Drawing.Size(75, 23);
			this.OKButton.TabIndex = 2;
			this.OKButton.Text = "OK";
			this.OKButton.UseVisualStyleBackColor = true;
			// 
			// ErrorDialog
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.BackgroundImage = ((System.Drawing.Image)(resources.GetObject("$this.BackgroundImage")));
			this.BackgroundImageLayout = System.Windows.Forms.ImageLayout.Stretch;
			this.ClientSize = new System.Drawing.Size(501, 254);
			this.Controls.Add(this.OKButton);
			this.Controls.Add(this.ErrorTextBox);
			this.Controls.Add(this.label1);
			this.DoubleBuffered = true;
			this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog;
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.MaximizeBox = false;
			this.Name = "ErrorDialog";
			this.Text = "Error";
			this.ResumeLayout(false);
			this.PerformLayout();
		}
		private System.Windows.Forms.Button OKButton;
		public System.Windows.Forms.TextBox ErrorTextBox;
		private System.Windows.Forms.Label label1;
	}
}
