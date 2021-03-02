using Microsoft.WindowsAzure.Storage.Blob;
using Microsoft.WindowsAzure.Storage.Queue;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace MP3SampleGenerator
{
    public partial class Default : System.Web.UI.Page
    {
        // accessor variables and methods for blob containers and queues
        private BlobStorageService _blobStorageService = new BlobStorageService();
        private CloudQueueService _queueStorageService = new CloudQueueService();

        private CloudBlobContainer getAudioGalleryContainer()
        {
            return _blobStorageService.getCloudBlobContainer();
        }

        private CloudQueue getAudioCreatorQueue()
        {
            return _queueStorageService.getCloudQueue();
        }


        private string GetMimeType(string Filename)
        {
            try
            {
                string ext = Path.GetExtension(Filename).ToLowerInvariant();
                Microsoft.Win32.RegistryKey key = Microsoft.Win32.Registry.ClassesRoot.OpenSubKey(ext);
                if (key != null)
                {
                    string contentType = key.GetValue("Content Type") as String;
                    if (!String.IsNullOrEmpty(contentType))
                    {
                        return contentType;
                    }
                }
            }
            catch
            {
            }
            return "application/octet-stream";
        }

        // User clicked the "Submit" button
        protected void submitButton_Click(object sender, EventArgs e)
        {
            if (upload.HasFile)
            {
                    // Get the file name specified by the user. 
                    var fileName = "." + Path.GetFileName(upload.FileName);

                    // Add more information to it so as to make it unique
                    // within all the files in that blob container
                    var name = string.Format("{0}{1}", Guid.NewGuid(), fileName);

                    // Upload photo to the cloud. Store it in a new 
                    // blob in the specified blob container. 

                    // Go to the container, instantiate a new blob
                    // with the descriptive name
                    String path = "audiofiles/" + name;

                    var blob = getAudioGalleryContainer().GetBlockBlobReference(path);

                    // The blob properties object (the label on the bucket)
                    // contains an entry for MIME type. Set that property.
                    blob.Properties.ContentType = GetMimeType(upload.FileName);

                    // Actually upload the data to the
                    // newly instantiated blob
                    blob.UploadFromStream(upload.FileContent);

                    // Place a message in the queue to tell the worker
                    // role that a new photo blob exists, which will 
                    // cause it to create a thumbnail blob of that photo
                    // for easier display. 
                    getAudioCreatorQueue().AddMessage(new CloudQueueMessage(System.Text.Encoding.UTF8.GetBytes(name)));

                    System.Diagnostics.Trace.WriteLine(String.Format("*** WebRole: Enqueued '{0}'", path));
                
            }
        }

        //Get the metadata for each blob 
        public String getBlobMetaData(Uri blobUri)
        {
            CloudBlockBlob blob = new CloudBlockBlob(blobUri);
            blob.FetchAttributes();
            return blob.Metadata["Title"];
        }

        protected void Page_PreRender(object sender, EventArgs e)
        {
            try
            {

                AudioDisplayControl.DataSource = from o in getAudioGalleryContainer().GetDirectoryReference("audiosamples").ListBlobs()
                                                 select new
                                                 {
                                                     Url = o.Uri,
                                                     Title = getBlobMetaData(o.Uri) //Calls getBlobMetaData to get metadata information for each entry
                                                 };

                // Tell the list view to bind to its data source, thereby
                // showing 
                AudioDisplayControl.DataBind();
            }
            catch (Exception)
            {
            }
        }
    }
}
