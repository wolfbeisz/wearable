using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage;
using App2.model;

namespace App2.dao
{
    public interface IScreenGraphLoader
    {
        /// <summary>
        /// Loads the ScreenGraph from the specified SQLite DB.
        /// 
        /// </summary>
        /// <param name="file"></param>
        /// <returns></returns>
        Task<ScreenGraph> LoadFromFile(StorageFile file);
    }
}
