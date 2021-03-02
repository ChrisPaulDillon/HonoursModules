using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Azure.WebJobs;

namespace MP3SampleGenerator_WebJob
{
    public class Program
    {
        // Please set the following connection strings in app.config for this WebJob to run.
        // These must be set with these names in WebJobs:-
        // AzureWebJobsDashboard and AzureWebJobsStorage
        static void Main()
        {
            var config = new JobHostConfiguration();

            if (config.IsDevelopment)
            {
                config.UseDevelopmentSettings();
            }

            // Default queue check has an exponentially increasing backoff interval.
            // Optionally can set a cap on how long the polling interval will increase to.

            config.Queues.MaxPollingInterval = TimeSpan.FromSeconds(5);

            var host = new JobHost(config);

            // The following code ensures that the WebJob will be running continuously.

            host.RunAndBlock();
        }
    }
}

