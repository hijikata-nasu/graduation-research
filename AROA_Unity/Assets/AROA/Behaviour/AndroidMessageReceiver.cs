using System;
using UnityEngine;

namespace AROA.Behaviour
{
    public class AndroidMessageReceiver : MonoBehaviour
    {
        [SerializeField] private DistanceUI distanceUI;
        [SerializeField] private InfoUI infoUI;

        private void Update()
        {
            SetDistance("150");
            SetRank("15");
            SetTime("3737");
        }

        public void SetDistance(string message)
        {
            int distance = StringToInt(message);

            distanceUI.SetDistance(distance);
            infoUI.SetDistance(distance);
        }

        public void SetRank(string message)
        {
            int rank = StringToInt(message);
            infoUI.SetRank(rank);
        }

        public void SetTime(string message)
        {
            int time = StringToInt(message);
            infoUI.SetTime(time);
        }

        private int StringToInt(string str)
        {
            int result;

            try
            {
                result = int.Parse(str);
            }
            catch (FormatException e)
            {
                Console.WriteLine("AndroidMessageReceiver");
                Console.WriteLine(e);
                throw;
            }

            return result;
        }
    }
}