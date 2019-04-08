package com.qiang.keyboard.utlis

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.preference.PreferenceManager
import com.daniel.edge.config.EdgeConfig
import com.daniel.edge.utils.log.EdgeLog
import com.qiang.keyboard.R
import com.qiang.keyboard.constant.SPConfig
import java.lang.Exception

class AudioUtils {
    //    /**设置播放类型
//     * @param type
//     */
//    fun setType(type: AudioType) {
//        EdgeSharePreferencesUtils.setSPProperty(SPConfig.AUDIO, SPConfig.AUDIO_TYPE, type.ordinal)
//        AUDIOTYPE = type
//        flag = 0
//    }

    //播放类型
    var AUDIOTYPE = AudioType.Mechanical
    //播放第几个声音
    private var flag = 0
    /**
     * 播放音频
     */
    var mClickSystemTime = 0L
    private var soundId = 0
    //    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool

    fun sound(key: String) {
        //禁止快速点击发声，否则会导致手指停止点击声音继续播放的问题
        val currentTime = System.currentTimeMillis()
        if (currentTime - mClickSystemTime >= 50) {
            mClickSystemTime = currentTime
            soundId = setDataSource(key)
        } else {
            mClickSystemTime = currentTime
        }
    }

    //设置播放资源
    private fun setDataSource(key: String): Int {
        var soundId = 0
        when (AUDIOTYPE) {
            AudioType.No -> {
            }
            AudioType.Mechanical -> {
                if (flag < 5) {
                    ++flag
                } else {
                    flag = 1
                }
                when (flag) {
                    1 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.mechanical_key_01, 1)
//                        mediaPlayer = MediaPlayer.create(EdgeConfig.CONTEXT, R.raw.mechanical_key_01)
                    2 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.mechanical_key_02, 1)
//                        mediaPlayer = MediaPlayer.create(EdgeConfig.CONTEXT, R.raw.mechanical_key_02)
                    3 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.mechanical_key_03, 1)
//                        mediaPlayer = MediaPlayer.create(EdgeConfig.CONTEXT, R.raw.mechanical_key_03)
                    4 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.mechanical_key_04, 1)
//                        mediaPlayer = MediaPlayer.create(EdgeConfig.CONTEXT, R.raw.mechanical_key_04)
                    5 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.mechanical_key_05, 1)
//                        mediaPlayer = MediaPlayer.create(EdgeConfig.CONTEXT, R.raw.mechanical_key_05)
                    else -> {
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.mechanical_key_01, 1)
//                        mediaPlayer = MediaPlayer.create(EdgeConfig.CONTEXT, R.raw.mechanical_key_01)
                        flag = 0
                    }
                }
            }
            AudioType.System -> {
                if (key.equals("Enter")) {
                    soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.system_enter, 1)
                } else {
                    soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.system_key, 1)
                }
                flag = 0
            }
            AudioType.Typewriter -> {
                if (key.equals("Enter")) {
                    soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_enter, 1)
                    return soundId
                }
                if (flag < 5) {
                    ++flag
                } else {
                    flag = 1
                }
                when (flag) {
                    1 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_key_01, 1)
                    2 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_key_02, 1)
                    3 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_key_03, 1)
                    4 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_key_04, 1)
                    5 ->
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_key_05, 1)
                    else -> {
                        soundId = soundPool.load(EdgeConfig.CONTEXT, R.raw.typewriter_key_01, 1)
                        flag = 0
                    }
                }
            }
        }
        return soundId
    }

    constructor() {
        var attrBuilder = AudioAttributes.Builder()
            .setLegacyStreamType(AudioManager.STREAM_MUSIC)
            .build()
        soundPool = SoundPool.Builder()
            .setMaxStreams(3)
            .setAudioAttributes(attrBuilder)
            .build()
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status ->
            if (status == 0) {
                soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f)
            }
        }
    }

    enum class AudioType {
        No, Mechanical, System, Typewriter;
    }

    companion object {
        fun getInstance() = Instance.INSTANCE.apply {
            //获取系统设置的音效类型
            try {
                AUDIOTYPE =
                    AudioType.values()[PreferenceManager.getDefaultSharedPreferences(EdgeConfig.CONTEXT).getString(
                        SPConfig.AUDIO_TYPE,
                        "1"
                    ).toInt()]
            } catch (e: Exception) {
                AUDIOTYPE = AudioType.Mechanical
            }
        }
    }

    object Instance {
        val INSTANCE = AudioUtils()
    }
}