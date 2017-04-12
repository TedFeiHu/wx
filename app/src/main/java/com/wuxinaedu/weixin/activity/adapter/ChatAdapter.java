package com.wuxinaedu.weixin.activity.adapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.image.ImageOptions;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.ImageDetailActivity;
import com.wuxinaedu.weixin.bean.Chat;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.ExpressionUtil;
import com.wuxinaedu.weixin.utils.ImageUtil;
import com.wuxinaedu.weixin.widget.RoundImageView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatAdapter extends BaseAdapter{

	private MediaPlayer player;
	private Context context;
	private List<Chat> list;
	private List<String> listUri;
	
	public ChatAdapter(Context context,List<Chat> list,MediaPlayer player){
		this.context=context;
		this.list=list;
		this.player = player;
	}
	
	public void setList(List<Chat> list) {
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Chat chat=list.get(position);
	    final ViewHolder holder;
		if(chat.getId()==0){
			if(convertView==null){
				holder = new ViewHolder();
				convertView=LayoutInflater.from(context).inflate(R.layout.chat_item, null);
				holder.meHead=(RoundImageView) convertView.findViewById(R.id.chat_me_head);
				holder.time=(TextView) convertView.findViewById(R.id.sub_det_time);
				holder.meContent=(TextView) convertView.findViewById(R.id.chat_me_text);
				holder.meImage=(ImageView) convertView.findViewById(R.id.chat_me_image);
				holder.meVoice=(ImageView) convertView.findViewById(R.id.chat_me_voice);
				holder.meVoice.setTag(position );
				
				
				final ImageView  im = holder.meVoice;
				//监听事件优化 TODO
				
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder) convertView.getTag();
			}
			
			//设置头像
			x.image().loadDrawable(chat.getMyHead(),ImageOptions.DEFAULT,new CommonCallback<Drawable>() {
				
				@Override
				public void onSuccess(Drawable arg0) {
					holder.meHead.setImageDrawable(arg0);
				}
				
				@Override
				public void onFinished() {
				}
				
				@Override
				public void onError(Throwable arg0, boolean arg1) {
				}
				
				@Override
				public void onCancelled(CancelledException arg0) {
					
				}
			});
			
			//时间
			holder.time.setText(chat.getTime());
			switch (chat.getType()) {
				case 0: //文字类型 
					holder.meVoice.setVisibility(View.GONE);
					holder.meImage.setVisibility(View.GONE);
					holder.meContent.setVisibility(View.VISIBLE);
					SpannableString content = ExpressionUtil.decorateFaceInStr(context,chat.getContent());
					holder.meContent.setText(content);
					break;
				case 1:
					holder.meVoice.setVisibility(View.GONE);
					holder.meContent.setVisibility(View.GONE);
					holder.meImage.setVisibility(View.VISIBLE);
					holder.meImage.setImageBitmap(ImageUtil.getSmallBitmap(chat.getImageUrl(), 150, 150));
					holder.meImage.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							List<Chat> list = getUrlList();
							int pos = list.indexOf(chat);
							Intent intent = new Intent(context,ImageDetailActivity.class);
							intent.putExtra(Constant.GET_SERIALIZABLE,(Serializable)listUri);
							intent.putExtra(Constant.GET_POSITION, pos);
							context.startActivity(intent);
						}
					});
					break;
				case 2:
					holder.meImage.setVisibility(View.GONE);
					holder.meContent.setVisibility(View.GONE);
					holder.meVoice.setVisibility(View.VISIBLE);
					final AnimationDrawable animationDrawable = (AnimationDrawable) holder.meVoice.getDrawable();
					holder.meVoice.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							animationDrawable.start();
							play(chat.getAudioUrl(),animationDrawable);
						}
					});
					break;
	
				default:
					break;
			}
		}else {
			//收到的消息 TODO
		}
		return convertView;
	}


	
	/**
	 * 开始播放
	 * @param url
	 */
	private void play(String url,final AnimationDrawable animationDrawable){
		if(player!=null){
			try{
				player.reset();
				player.setDataSource(url);
				player.prepare();
				player.start();
				player.setOnCompletionListener(new OnCompletionListener() {
					
					@Override
					public void onCompletion(MediaPlayer mp) {
						animationDrawable.stop();
					}
				});
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 停止播放
	 */
	public void stop(){
		if(player!=null && player.isPlaying()){
			player.stop();
		}
	}
	
	/**
	 * 获取图片URL
	 * 遍历集合  如果有图片则将图片url加入集合
	 * @param list  图片url
	 */
	public List<Chat> getUrlList(){
		listUri = new ArrayList<>();
		List<Chat> urlList = new ArrayList<>();
		if(null!=list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Chat chat = list.get(i);
				if(Chat.IMAGE == chat.getType()){
					urlList.add(chat);
					listUri.add(chat.getImageUrl());
				}
			}
		}
		return urlList;
	}
	
	class ViewHolder{
		TextView time;
		RoundImageView peerHead;
		TextView peerContent;
		RoundImageView meHead;
		TextView meContent;
		ImageView meImage;
		ImageView meVoice;
	}
}
