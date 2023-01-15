using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Serialization;

namespace AROA.Behaviour
{
    public class FollowCamera : MonoBehaviour
    {
        [SerializeField] private Transform cameraTransform;
        [SerializeField] private float speed = 2f;
        [SerializeField] private float distance = 1f;
        [SerializeField] private bool isDelay = true;
        [SerializeField] private float verticalOffset = 0;

        private void Update()
        {
            Vector3 direction = cameraTransform.forward;
            direction.y = 0;
            Vector3 verticalOffsetVector = new Vector3(0, verticalOffset, 0);
            Vector3 targetPosition = cameraTransform.position + (direction * distance) + verticalOffsetVector;
            Quaternion targetRotation = Quaternion.LookRotation(targetPosition);

            if (isDelay)
            {
            float t = Time.deltaTime * speed;
            Vector3 position = Vector3.Lerp(transform.position, targetPosition, t);
            Quaternion rotation = Quaternion.Lerp(transform.rotation, targetRotation, t);
            transform.SetPositionAndRotation(position, rotation);
            }
            else
            {
                transform.SetPositionAndRotation(targetPosition, targetRotation);
            }
        }
    }
}