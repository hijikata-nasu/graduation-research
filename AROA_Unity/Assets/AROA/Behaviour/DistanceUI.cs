using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace AROA.Behaviour
{
    public class DistanceUI : MonoBehaviour
    {
        [SerializeField] private Transform targetCircle;
        [SerializeField] private Transform zeroCircle;
        [SerializeField] private float zeroScale = 100;

        void Start()
        {
            zeroCircle.localScale = new Vector3(zeroScale, zeroScale, zeroCircle.localScale.z);
            targetCircle.localScale = new Vector3(zeroScale, zeroScale, targetCircle.localScale.z);
        }

        public void SetDistance(int distance)
        {
            if (distance < 0)
            {
                targetCircle.localScale =
                    new Vector3(zeroScale, zeroScale, targetCircle.localScale.z);
            }
            else
            {
                targetCircle.localScale =
                    new Vector3(zeroScale + distance, zeroScale + distance, targetCircle.localScale.z);
            }
        }
    }
}