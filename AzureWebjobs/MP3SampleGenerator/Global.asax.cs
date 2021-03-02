using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Configuration;
using System.Web.Security;
using System.Web.SessionState;

namespace MP3SampleGenerator
{
    public class Global : System.Web.HttpApplication
    {
        protected void Application_Start(object sender, EventArgs e)
        {
        }

        protected void Application_EndRequest(object sender, EventArgs e)
        {
            //check for the "file is too big" exception if thrown at the IIS level
            if (Response.StatusCode == 404 && Response.SubStatusCode == 13)
            {
                Response.Write("Error - File too big, please refresh page!"); 
                Response.End();
            }
        }
    }
}
