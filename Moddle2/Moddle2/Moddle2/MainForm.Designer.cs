namespace DiamondSwordDev.Moddle2
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
			this.ModpackPanel = new System.Windows.Forms.Panel();
			this.ModpackVerticalScrollBar = new System.Windows.Forms.VScrollBar();
			this.label1 = new System.Windows.Forms.Label();
			this.UsernameBox = new System.Windows.Forms.ComboBox();
			this.label2 = new System.Windows.Forms.Label();
			this.PasswordBox = new System.Windows.Forms.TextBox();
			this.button1 = new System.Windows.Forms.Button();
			this.label3 = new System.Windows.Forms.Label();
			this.ModpackPanel.SuspendLayout();
			this.SuspendLayout();
			// 
			// ModpackPanel
			// 
			this.ModpackPanel.BackColor = System.Drawing.Color.FromArgb(((int)(((byte)(224)))), ((int)(((byte)(224)))), ((int)(((byte)(224)))));
			this.ModpackPanel.BorderStyle = System.Windows.Forms.BorderStyle.FixedSingle;
			this.ModpackPanel.Controls.Add(this.ModpackVerticalScrollBar);
			this.ModpackPanel.Location = new System.Drawing.Point(12, 12);
			this.ModpackPanel.Name = "ModpackPanel";
			this.ModpackPanel.Size = new System.Drawing.Size(188, 324);
			this.ModpackPanel.TabIndex = 0;
			// 
			// ModpackVerticalScrollBar
			// 
			this.ModpackVerticalScrollBar.Location = new System.Drawing.Point(170, -1);
			this.ModpackVerticalScrollBar.Name = "ModpackVerticalScrollBar";
			this.ModpackVerticalScrollBar.Size = new System.Drawing.Size(17, 324);
			this.ModpackVerticalScrollBar.TabIndex = 0;
			this.ModpackVerticalScrollBar.Scroll += new System.Windows.Forms.ScrollEventHandler(this.ModpackVerticalScrollBarScroll);
			// 
			// label1
			// 
			this.label1.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.label1.Location = new System.Drawing.Point(12, 358);
			this.label1.Name = "label1";
			this.label1.Size = new System.Drawing.Size(66, 15);
			this.label1.TabIndex = 1;
			this.label1.Text = "Username:";
			// 
			// UsernameBox
			// 
			this.UsernameBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.UsernameBox.FormattingEnabled = true;
			this.UsernameBox.Location = new System.Drawing.Point(84, 355);
			this.UsernameBox.Name = "UsernameBox";
			this.UsernameBox.Size = new System.Drawing.Size(201, 22);
			this.UsernameBox.TabIndex = 2;
			// 
			// label2
			// 
			this.label2.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.label2.Location = new System.Drawing.Point(12, 386);
			this.label2.Name = "label2";
			this.label2.Size = new System.Drawing.Size(66, 15);
			this.label2.TabIndex = 3;
			this.label2.Text = "Password:";
			// 
			// PasswordBox
			// 
			this.PasswordBox.Font = new System.Drawing.Font("Tahoma", 9F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.PasswordBox.Location = new System.Drawing.Point(84, 383);
			this.PasswordBox.Name = "PasswordBox";
			this.PasswordBox.PasswordChar = '●';
			this.PasswordBox.Size = new System.Drawing.Size(201, 22);
			this.PasswordBox.TabIndex = 4;
			// 
			// button1
			// 
			this.button1.Font = new System.Drawing.Font("Tahoma", 12F, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
			this.button1.Location = new System.Drawing.Point(579, 358);
			this.button1.Name = "button1";
			this.button1.Size = new System.Drawing.Size(143, 45);
			this.button1.TabIndex = 5;
			this.button1.Text = "Play";
			this.button1.UseVisualStyleBackColor = true;
			// 
			// label3
			// 
			this.label3.Location = new System.Drawing.Point(421, 160);
			this.label3.Name = "label3";
			this.label3.Size = new System.Drawing.Size(100, 23);
			this.label3.TabIndex = 6;
			this.label3.Text = "Nothing here yet.";
			// 
			// MainForm
			// 
			this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
			this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
			this.BackColor = System.Drawing.Color.WhiteSmoke;
			this.ClientSize = new System.Drawing.Size(734, 412);
			this.Controls.Add(this.label3);
			this.Controls.Add(this.button1);
			this.Controls.Add(this.PasswordBox);
			this.Controls.Add(this.label2);
			this.Controls.Add(this.UsernameBox);
			this.Controls.Add(this.label1);
			this.Controls.Add(this.ModpackPanel);
			this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
			this.Name = "MainForm";
			this.Text = "Moddle Launcher";
			this.Load += new System.EventHandler(this.MainFormLoad);
			this.ModpackPanel.ResumeLayout(false);
			this.ResumeLayout(false);
			this.PerformLayout();
		}
		private System.Windows.Forms.Label label3;
		private System.Windows.Forms.VScrollBar ModpackVerticalScrollBar;
		private System.Windows.Forms.Button button1;
		private System.Windows.Forms.TextBox PasswordBox;
		private System.Windows.Forms.Label label2;
		private System.Windows.Forms.ComboBox UsernameBox;
		private System.Windows.Forms.Label label1;
		private System.Windows.Forms.Panel ModpackPanel;
	}
}
