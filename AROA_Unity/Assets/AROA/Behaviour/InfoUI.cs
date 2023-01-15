using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class InfoUI : MonoBehaviour
{
    [SerializeField] private TextMeshProUGUI timeTMP;
    [SerializeField] private TextMeshProUGUI rankTMP;
    [SerializeField] private TextMeshProUGUI distanceTMP;

    private void Start()
    {
        timeTMP.text = "Debug:No time";
        rankTMP.text = "Debug:No rank";
        distanceTMP.text = "Debug:No distance";
    }

    public void SetTime(int time)
    {
        int timeHour;
        int timeMinute;
        int timeSecond;

        if (time < 0)
        {
            timeHour = 0;
            timeMinute = 0;
            timeSecond = 0;
        }
        else
        {
            timeHour = time / 3600;
            timeMinute = (time % 3600) / 60;
            timeSecond = (time % 3600) % 60;
        }

        timeTMP.text = $"{timeHour}時間{timeMinute}分{timeSecond}秒";
    }

    public void SetRank(int rank)
    {
        if (rank <= 0)
        {
            rankTMP.text = "順位不明";
        }
        else
        {
            rankTMP.text = $"{rank}位";
        }
    }

    public void SetDistance(int distance)
    {
        distanceTMP.text = $"{distance}m";
    }
}