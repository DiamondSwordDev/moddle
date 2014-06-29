/*
 * Created by SharpDevelop.
 * User: LukeSmalley
 * Date: 3/11/2014
 * Time: 2:35 PM
 * 
 * To change this template use Tools | Options | Coding | Edit Standard Headers.
 */

using System;
using System.Drawing;
using System.Windows.Forms;

namespace DiamondSwordDev.Moddle2
{
	public class ModpackPictureBox : PictureBox
	{
		public string ModpackName = "";
		
		public ModpackPictureBox()
		{
			this.Size = new Size(160, 96);
		}
		
		public ModpackPictureBox(string modpackName)
		{
			this.Size = new Size(160, 96);
			this.ModpackName = modpackName;
			this.Image = new Bitmap(AppDomain.CurrentDomain.BaseDirectory + "launcher\\" + modpackName + "\\" + modpackName + ".png");
		}
	}
}