package com.tuneer.tmplayer

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper

import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import kotlinx.coroutines.Runnable
import java.lang.Exception

class MiniMediaPlayer() {
	
	var mediaPlayer:MediaPlayer?=null
	var playImageDrawable:Int?=null
	var pauseImageDrawable:Int?=null
	var stopImageDrawable:Int?=null
	var stopIconRequired:Boolean?=false
	var playPauseButton:ImageView?=null
	
	constructor(context: Context,soundPathUri: Uri) : this() {
		mediaPlayer = MediaPlayer.create(context,soundPathUri)
		createMiniUI(context)
	}
	
	constructor(context: Context,path: String) : this() {
		mediaPlayer = MediaPlayer()
		mediaPlayer?.setDataSource(path)
		createMiniUI(context)
	}
	
	
	
	
	private fun MiniMediaPlayer.addPlayIcon(image:Int){
		playImageDrawable=image
	}
	private fun MiniMediaPlayer.addPauseIcon(image:Int){
		pauseImageDrawable=image
	}
	
	private fun MiniMediaPlayer.addStopIcon(image:Int,required:Boolean){
		stopImageDrawable=image
		stopIconRequired=required
	}
	
	
	private fun MiniMediaPlayer.play(){
		if (mediaPlayer!=null){
			mediaPlayer?.start()
			changeIcon()
		}
		
	}
	
	private fun MiniMediaPlayer.prepare(){
		if (mediaPlayer!=null){
			mediaPlayer?.prepare()
		}
		
	}
	
	private fun MiniMediaPlayer.prepareAsync(){
		if (mediaPlayer!=null){
			mediaPlayer?.prepareAsync()
		}
		
	}
	
	private fun changeIcon() {
		playPauseButton?.setImageResource(stopImageDrawable!!)
	}
	
	private fun MiniMediaPlayer.pause(){
		if (mediaPlayer!=null)mediaPlayer?.pause()
	}
	
	private fun MiniMediaPlayer.stop(){
		if (mediaPlayer!=null) {
			mediaPlayer?.stop()
			mediaPlayer?.reset()
			mediaPlayer?.release()
			mediaPlayer=null
		}
	}
	
	private fun createMiniUI(
		context: Context) {
		
				
				val constraintLayout = ConstraintLayout(context)
				constraintLayout.layoutParams = ConstraintLayout.LayoutParams(
					ConstraintLayout.LayoutParams.MATCH_PARENT,
					ConstraintLayout.LayoutParams.WRAP_CONTENT
				)
				
				playPauseButton = ImageView(context)
				playPauseButton!!.id = View.generateViewId()
				playPauseButton!!.setImageResource(playImageDrawable!!)
				playPauseButton!!.setPadding(10, 10, 10, 10)
				constraintLayout.addView(playPauseButton)
				
				val seekBar = SeekBar(context)
				seekBar.id = View.generateViewId()
				constraintLayout.addView(seekBar)
				
				val stopButton = ImageView(context)
				stopButton.id = View.generateViewId()
				stopButton.setImageResource(stopImageDrawable!!)
				stopButton.setPadding(10, 10, 10, 10)
		        if (this.stopIconRequired!!) {
		 	       constraintLayout.addView(stopButton)
				}
				
				val set = ConstraintSet()
				set.clone(constraintLayout)
				
				// Constraints for playPauseButton
				set.connect(playPauseButton!!.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
				set.connect(playPauseButton!!.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
				set.connect(playPauseButton!!.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
				
				// Constraints for seekBar
				set.connect(seekBar.id, ConstraintSet.START, playPauseButton!!.id, ConstraintSet.END)
				set.connect(seekBar.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
				set.connect(seekBar.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
		
		if (this.stopIconRequired!!) {
			// Constraints for stopButton
			set.connect(seekBar.id, ConstraintSet.END, stopButton.id, ConstraintSet.START)
			set.connect(
				stopButton.id,
				ConstraintSet.TOP,
				ConstraintSet.PARENT_ID,
				ConstraintSet.TOP
			)
			set.connect(
				stopButton.id,
				ConstraintSet.BOTTOM,
				ConstraintSet.PARENT_ID,
				ConstraintSet.BOTTOM
			)
			set.connect(
				stopButton.id,
				ConstraintSet.END,
				ConstraintSet.PARENT_ID,
				ConstraintSet.END
			)
		}else{
			set.connect(seekBar.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
		}
		
		set.applyTo(constraintLayout)
		
		seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
			override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
				if (fromUser)mediaPlayer?.seekTo(progress)
			}
			
			override fun onStartTrackingTouch(seekBar: SeekBar?) {
			}
			
			override fun onStopTrackingTouch(seekBar: SeekBar?) {
			}
		})
		
		initiateSeekBar(seekBar)
				
		
		}
	
	private fun initiateSeekBar(seekBar: SeekBar) {
		seekBar.max = mediaPlayer?.duration!!
		val handler = Handler(Looper.myLooper()!!)
		// Create a Runnable to update the SeekBar
		val updateSeekBarRunnable = object : Runnable {
			override fun run() {
				try {
					// Update the SeekBar with the current position of the MediaPlayer
					seekBar.progress = mediaPlayer?.currentPosition ?: 0
					
					// Post the Runnable with a delay of 1000 milliseconds (1 second)
					handler.postDelayed(this, 1000)
				} catch (ex: Exception) {
					// Handle exceptions, e.g., when the MediaPlayer is not initialized
					seekBar.progress = 0
				}
			}
		}

       // Start the Runnable with an initial delay of 0 milliseconds
		handler.postDelayed(updateSeekBarRunnable, 0)
	}
	
	
}