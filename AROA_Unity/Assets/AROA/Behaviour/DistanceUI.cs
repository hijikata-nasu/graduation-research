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
        private float _distance = 100;

        public float Distance
        {
            get => _distance;
            set
            {
                if (value < 0)
                {
                    _distance = 0;
                    return;
                }

                _distance = value;
            }
        }

        void Update()
        {
            zeroCircle.localScale = new Vector3(zeroScale, zeroScale, zeroCircle.localScale.z);
            targetCircle.localScale =
                new Vector3(zeroScale + _distance, zeroScale + _distance, targetCircle.localScale.z);
        }
    }
}