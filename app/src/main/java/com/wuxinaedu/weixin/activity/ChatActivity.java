package com.wuxinaedu.weixin.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.wuxinaedu.weixin.R;
import com.wuxinaedu.weixin.activity.adapter.ChatAdapter;
import com.wuxinaedu.weixin.activity.core.BaseActivity;
import com.wuxinaedu.weixin.bean.Chat;
import com.wuxinaedu.weixin.bean.Contacts;
import com.wuxinaedu.weixin.bean.UserInfor;
import com.wuxinaedu.weixin.utils.Constant;
import com.wuxinaedu.weixin.utils.CoreUtil;
import com.wuxinaedu.weixin.utils.DateUtil;
import com.wuxinaedu.weixin.utils.ExpressionUtil;
import com.wuxinaedu.weixin.utils.FileLocalCache;
import com.wuxinaedu.weixin.utils.FileUtil;
import com.wuxinaedu.weixin.utils.ImageUtil;
import com.wuxinaedu.weixin.utils.L;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class ChatActivity extends BaseActivity implements OnClickListener,OnLayoutChangeListener{

	private MediaRecorder recoder;
	private MediaPlayer player;
	private String audioPath; //录音文件路径
	private long audioStartTime; //录音开始时间
	
	private boolean isSend = true; //是否取消发送音频
	
	private GridView menu,face;
	private EditText editText;
	private ImageView voiceAni;
	private TextView voiceTv;
	private View activityRootView,bottom,keyBoard,voice,faceIcon,sendVoice,send,add,speak,bottomMenu;
	private boolean firstKeyBoard = true;
	private InputMethodManager imm;
	private ListView listView;
	private List<Chat> list;
	private ChatAdapter adapter;
	private Contacts contacts;
	private UserInfor userInfor; 
	private String photoPath,uploadPhotoPath;
	private static final int IMAGES = 0,CEMERA = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取聊天的对象
		final Intent intent = getIntent();
		contacts = (Contacts) intent.getSerializableExtra(Constant.GET_SERIALIZABLE);
		//设置标题
		setTitleName(contacts.getName());
		showRightIv(R.drawable.icon_chat_user,new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent.setClass(ChatActivity.this, DetailsActivity.class);
				startActivity(intent);
			}
		});
		// 获得键盘管理服务
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		//为根视图设置视图改变的监听器，用于检测软键盘的状态
		activityRootView = findViewById(R.id.root_layout); 
		activityRootView.addOnLayoutChangeListener(this);
		
		//初始化界面
//		bottomMenu = findViewById(R.id.chat_bottom_ll); 
		bottom = findViewById(R.id.ll_bottom); 
		menu = (GridView) findViewById(R.id.menu);
		face = (GridView) findViewById(R.id.face);
		listView = (ListView) findViewById(R.id.lv_id);
		
		list = new ArrayList<>();
		//便于在activity 中回收player ，故在此页面创建
		recoder = new MediaRecorder();
		player = new MediaPlayer();
		
		adapter = new ChatAdapter(this,list,player);
		listView.setAdapter(adapter);
		
		menu.setAdapter(initMenu());
	
		
		faceIcon = findViewById(R.id.face_id);
		faceIcon.setOnClickListener(this);
		sendVoice = findViewById(R.id.send_voice_id);
		sendVoice.setOnClickListener(this);
		voice = findViewById(R.id.voice_id);
		voice.setOnClickListener(this);
		keyBoard = findViewById(R.id.keyboard_id);
		keyBoard.setOnClickListener(this);
		add = findViewById(R.id.add_icon_id);
		add.setOnClickListener(this);
		
		send = findViewById(R.id.send_id);
		send.setOnClickListener(this);
		editText = (EditText) findViewById(R.id.input_id);
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				//判读是够有文字，设置发送按钮的状态
				manageSendBtn();
			}
		});
		/**
		 * 当输入框获得焦点的时候  隐藏菜单
		 */
//		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
//			
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(hasFocus){
//					menu.setVisibility(View.GONE);
//					face.setVisibility(View.GONE);
//				}
//			}
//		});
		
		speak =findViewById(R.id.speak);
		voiceAni = (ImageView) findViewById(R.id.speak_iv);
		voiceTv = (TextView) findViewById(R.id.speak_tv);
		final AnimationDrawable animationDrawable =  (AnimationDrawable) voiceAni.getBackground();
		/**
		 * 发送语音按钮的触摸事件
		 */
		sendVoice.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()){
				case MotionEvent.ACTION_DOWN: //按下
					if (!FileUtil.isExistSD()) {
						toastMessage("没有SD卡读写权限。");
						return false;
					}
					animationDrawable.start();
					speak.setVisibility(View.VISIBLE);
					voiceTv.setText("向上滑动，取消发送");
					voiceTv.setBackgroundColor(Color.TRANSPARENT);
					//TODO 开始录音
					startRecord();
					return false;
				case MotionEvent.ACTION_MOVE: //移动
					if(event.getY()<0){
						voiceTv.setText("松开手指，取消发送");
						voiceTv.setBackgroundResource(R.drawable.speak_tv_background);
						isSend = false;
					}else{
						voiceTv.setText("向上滑动，取消发送");
						voiceTv.setBackgroundColor(Color.TRANSPARENT);
						isSend = true;
					}
					return false;
				case MotionEvent.ACTION_UP: //抬起
					//停止动画 隐藏动画
					animationDrawable.stop(); 
					speak.setVisibility(View.GONE);
					//TODO 停止录音    同时判断录音时间
					//小于1s不发送 删除文件
					stopRecord();
					if(isSend){  
						sendAudio();
					}
					return false;
				default:
					speak.setVisibility(View.GONE);
					return false;
				}
			}
		});
		
		final List<Map<String, Object>>  faceList = ExpressionUtil.buildExpressionsList(this);
		// 表情
		face.setAdapter(new SimpleAdapter(this,faceList,R.layout.item_face,
				new String[]{"drawableRId"},new int[]{R.id.im_id}));
		face.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//将表情插入到光标指定的位置
				
				//获取加入文字长度
				String  face = (String) faceList.get(position).get("drawableId");
				face = ExpressionUtil.drawableIdToFaceName.get(face);
				//获取文字选中的位置
				int select = editText.getSelectionStart();
				
				//将文字进行转换成 复合文本
				String str = editText.getText().toString();
				StringBuffer sBuffer = new StringBuffer(str);
				str = sBuffer.insert(select,face).toString();
				SpannableString  ss = ExpressionUtil.decorateFaceInStr(ChatActivity.this,str);
				
				//将转换过的文字设置到输入框
				editText.setText(ss);
				//设置光标选中的位置
				editText.setSelection(select+face.length());
			}
		});
		
		menu.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
				case 0://发送图片
					Intent intent;
					//判断是否为4.4或以上版本。
					if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
						intent = new Intent(Intent.ACTION_PICK,Media.EXTERNAL_CONTENT_URI);
					}else {
						intent = new Intent(Intent.ACTION_GET_CONTENT,null);
					}
					//设置 获取数据类型为 图片
					intent.setDataAndType(Media.EXTERNAL_CONTENT_URI,"image/*");					
					startActivityForResult(intent,IMAGES);
					break;
				case 1: //拍照
					Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					String dir = FileUtil.getSDCardPath()+"A微信/图片/"; //定义一个文件夹储存图片
					FileUtil.checkDir(dir); //判断文件夹是否存在，不存在创建
					File file = new File(dir,"image"+System.currentTimeMillis()+".jpg");  //为拍照图片命名。
					photoPath = file.getAbsolutePath();  //获取文件存储目录
					Uri uri = Uri.fromFile(file);    //创建uri
					//启动相机，将照片存放发哦uri位置
					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
					startActivityForResult(cameraIntent,CEMERA);
					break;

				default:
					toastMessage("点击事件");
					break;
				}
			}
		});
		
		//获取用户信息
		userInfor = (UserInfor) FileLocalCache.getSerializableData(this,Constant.USER_INFOR);
		
		
	}

	@Override
	protected int getContentView() {
		return R.layout.activity_chat;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IMAGES: //发送图片界面返回
			if(resultCode == 0 || null == data){ //无数据返回不发送
				return;
			}
			Uri uri = data.getData();
			//获得图片路径
			uploadPhotoPath = ImageUtil.getRealFilePath(this, uri);
			sendPicture();
			break;
		case CEMERA: //相机界面返回
			if(resultCode==0){ //无数据返回不发送   有数据返回，返回-1
				return;
			}
			uploadPhotoPath = photoPath;
			sendPicture();
			break;

		default:
			break;
		}

		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.add_icon_id:  //点击加号 显示菜单
			manageMenu();
			break;
		case R.id.send_id: //发送文字
			sendText();
			break;
		case R.id.face_id:
			manageFace();
			break;
		case R.id.keyboard_id:
			sendVoice.setVisibility(View.GONE);
			keyBoard.setVisibility(View.GONE);
			voice.setVisibility(View.VISIBLE);
			editText.setVisibility(View.VISIBLE);
			faceIcon.setVisibility(View.VISIBLE);
			
			menu.setVisibility(View.GONE);
			face.setVisibility(View.GONE);
			manageSendBtn();
			//获得焦点，强制打开键盘
			editText.requestFocus();
			imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
			break;
		case R.id.voice_id:
			// 关闭软键盘
	        imm.hideSoftInputFromWindow(editText.getWindowToken(),0); 
	        
			sendVoice.setVisibility(View.VISIBLE);
			keyBoard.setVisibility(View.VISIBLE);
			voice.setVisibility(View.GONE);
			editText.setVisibility(View.GONE);
			faceIcon.setVisibility(View.GONE);
			
			menu.setVisibility(View.GONE);
			face.setVisibility(View.GONE);
			
			send.setVisibility(View.GONE);
			add.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		}
	}
	/**
	 * 发送文字
	 */
	private void sendText(){
		//构造一个聊天的对象，用于传递聊天信息
		Chat chat = new Chat();
		String content = editText.getText().toString();
		chat.setContent(content);
		chat.setId(Chat.ME);
		chat.setType(Chat.TEXT);
		chat.setTime(DateUtil.parseLongToString(System.currentTimeMillis(),DateUtil.SDF));
		//设置对方头像
//		chat.setHead(contacts.getHead());
		chat.setMyHead(userInfor.getHead());
		ExpressionUtil.decorateFaceInStr(this,content);
		
		//更新视图
		list.add(chat);
		adapter.setList(list);
		adapter.notifyDataSetChanged();
		listView.setSelection(list.size()-1);
		editText.setText("");
	} 
	
	/**
	 * 发送图片
	 */
	private void sendPicture(){
		if ((!TextUtils.isEmpty(uploadPhotoPath))) {
			Chat chat = new Chat();
			chat.setId(Chat.ME); 			 //设置 消息发送者为自己
			chat.setType(Chat.IMAGE);	 //消息类型
			chat.setTime(DateUtil.parseLongToString(System.currentTimeMillis(),DateUtil.SDF)); 	//格式化时间
			chat.setMyHead(userInfor.getHead());  		 //如果 需要对方头像的话   intent传值拿到头像 
			chat.setImageUrl(uploadPhotoPath);		 //设置图片uri
			list.add(chat);
			adapter.notifyDataSetChanged();
			listView.setSelection(list.size()-1);
		}
	}
	
	/**
	 * 发送语音
	 */
	public void sendAudio(){
		if(!TextUtils.isEmpty(audioPath)){
			Chat chat = new Chat();
			chat.setType(Chat.VOICE);
			chat.setTime(DateUtil.parseLongToString(System.currentTimeMillis(),DateUtil.SDF));
			chat.setMyHead(userInfor.getHead());    
			chat.setAudioUrl(audioPath);
			list.add(chat);
			adapter.notifyDataSetChanged();
			listView.setSelection(list.size()-1);
		}
	}
	
	/**
	 * 点击加号
	 * 如果显示就隐藏，隐藏就显示
	 * ------ TODO -----》》在发送语音按钮存在时，点击加号，应切换回文字输入界面
	 */
	private void manageMenu(){
		if(menu.getVisibility() == View.GONE){
			menu.setVisibility(View.VISIBLE);
			face.setVisibility(View.GONE);
		}else{
			menu.setVisibility(View.GONE);
		}
		//清除焦点 关闭软键盘
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0); 
        editText.clearFocus();
	}
	/**
	 * 点击表情按钮
	 * 如果显示就隐藏，隐藏就显示
	 */
	private void manageFace(){
		if(face.getVisibility() == View.GONE){
			face.setVisibility(View.VISIBLE);
			menu.setVisibility(View.GONE);
		}else{
			face.setVisibility(View.GONE);
		}
		// 关闭软键盘 不清除焦点
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);   
        imm.hideSoftInputFromWindow(editText.getWindowToken(),0); 
	}
	
	/**
	 * 处理发送按钮
	 */
	private void manageSendBtn(){
		//判断输入框内是否有文字 来改变 发送按钮的显示与隐藏  
		if(!CoreUtil.isEmpty(editText.getText().toString())){
			send.setVisibility(View.VISIBLE);
			add.setVisibility(View.GONE);
		}else{
			send.setVisibility(View.GONE);
			add.setVisibility(View.VISIBLE);
		}
	}


	/**
	 *  模仿微信中设置菜单高度的设置  TODO 作为补充
	 *  思路：
	 *  设置键盘的默认状态  第一次打开为android:windowSoftInputMode="adjustResize" 获取键盘高度后设置底部菜单的高度
	 *  第二次 设为这个  android:windowSoftInputMode="adjustPan"
	 *  （但此时布局不会改变，即不会再调用这个方法来关闭表情和菜单界面，须另寻他法）
	 */
	@Override
	public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight,
			int oldBottom) {
		//old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值  
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > 0)){  
        	//键盘弹出  隐藏表情和菜单界面
        	menu.setVisibility(View.GONE);
			face.setVisibility(View.GONE);
			//曲径通幽  将视图隐藏后再显示，达到刷新效果
			this.bottom.setVisibility(View.GONE);
			this.bottom.setVisibility(View.VISIBLE);
			//获得焦点，强制打开键盘
			editText.requestFocus();
			if(firstKeyBoard){
				L.e(oldBottom - bottom+"键盘高度---》》");
//				LayoutParams l=  menu.getLayoutParams();
//				l.height=oldBottom - bottom;
//				menu.setLayoutParams(l);
//				l= (LayoutParams) face.getLayoutParams();
//				l.height=oldBottom - bottom;
//				face.setLayoutParams(l);
				firstKeyBoard = false;
			}
//            Toast.makeText(this, "监听到软键盘弹起...高度为："+(oldBottom - bottom), Toast.LENGTH_SHORT).show();  
        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > 0)){  
//            Toast.makeText(this, "监听到软件盘关闭...", Toast.LENGTH_SHORT).show();  
        }  
	}
	
	/**
	 * 获得底部菜单数据。
	 */
	private SimpleAdapter initMenu(){
		Map<String,Object> map;
		List<Map<String,Object>> data=new ArrayList<>(); 
		map=new HashMap<>();
		map.put("image",R.drawable.chat_tool_photo);
		map.put("text",getString(R.string.pics));
		data.add(map);
		map=new HashMap<>();
		map.put("image",R.drawable.chat_tool_camera);
		map.put("text",getString(R.string.camera));
		data.add(map);
		map=new HashMap<>();
		map.put("image",R.drawable.chat_tool_location);
		map.put("text",getString(R.string.location));
		data.add(map);
		map=new HashMap<>();
		map.put("image",R.drawable.chat_tool_send_file);
		map.put("text",getString(R.string.file));
		data.add(map);
		map=new HashMap<>();
		map.put("image",R.drawable.chat_tool_audio);
		map.put("text",getString(R.string.voice_phone));
		data.add(map);
		map=new HashMap<>();
		map.put("image",R.drawable.chat_tool_video);
		map.put("text",getString(R.string.video));
		data.add(map);
		SimpleAdapter sa=new SimpleAdapter(this, data,R.layout.item_chat_bottom_menu,
				new String[]{"image","text"},new int[]{R.id.im_id,R.id.tv_id});
		return sa;
	}
	
	/**
	 * 开始录音
	 */
	public void startRecord(){
//		recoder.reset();
//		recoder.release();
		//设置录音相关的参数   
		//设置音源为 麦克风
		recoder.setAudioSource(MediaRecorder.AudioSource.MIC);
		//输出格式
		recoder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		//设置音频编码器
		recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		//设置缓存路径
		String str = FileUtil.getSDCardPath()+"/A微信/voice/";
		FileUtil.checkDir(str);  //查找 文件目录 不存在创建。
		//设置文件名
		audioPath = str + System.currentTimeMillis()+".amr";
		//设置输出路径
		recoder.setOutputFile(audioPath);
		
		try {
			//录音之前初始化
			recoder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//开始录音
		recoder.start();
		audioStartTime = System.currentTimeMillis();
	}
	/**
	 * 中途停止录音
	 */
	public void stopRecord(){
		if(recoder!=null){
			recoder.stop();
		}
		long time = System.currentTimeMillis()-audioStartTime;
		if(time<1000){
			//录音时间小于1秒,需要删除音频文件
			toastMessage("录音时间太短");
			File file = new File(audioPath);
			if(file.exists()){
				file.delete();
			}
			isSend = false;
		}
	}	
	
	/**
	 * 退出 停止录音
	 */
	@Override
	public void onBackPressed() {
		if(player!=null && player.isPlaying()){
			player.stop();
		}
		super.onBackPressed();
	}
	
	/**
	 * 退出界面 回收
	 */
	@Override
	protected void onDestroy() {
		if(recoder!=null){
			recoder.release();
			recoder = null;
		}
		
		if(player!=null){
			player.release();
			player = null;
		}
		super.onDestroy();
	}
}
