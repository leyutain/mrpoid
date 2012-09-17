package com.tszy.mrpoid;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tszy.fileex.LinuxFileCmd;

public class FileExActivity extends Activity implements OnItemClickListener, OnClickListener {
	public static final String ROOT_DIR = "/mnt/sdcard/mythroad";

	private String mCurPath = "/";
	private ArrayList<File> mFileList;
	private Bitmap fic_file, fic_dir;
	private FileAdapter mAdapter;
	private TextView leftText, rightText;
	private Button btn_back;
//	private boolean bRooted; // 获取ROOT标志
	private EditText editText;
	ListView listView;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_fileex);
		// 标题栏
		//getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);

		initVars();

		initViews();

		initRes();

		// 获取root权限
//		bRooted = LinuxShell.getRoot(this);

		firstSearch();
	}

	// 变量初始化
	private void initVars() {
		mFileList = new ArrayList<File>(100);
		mAdapter = new FileAdapter();
	}

	// 资源初始化
	private void initRes() {
		// 资源初始化
		fic_file = BitmapFactory.decodeResource(getResources(), R.drawable.smiley);
		fic_dir = BitmapFactory.decodeResource(getResources(), R.drawable.folder);
	}

	// 控件初始化
	private void initViews() {
		editText = (EditText)findViewById(R.id.edit_path);
		leftText = (TextView) findViewById(R.id.left_text);
		rightText = (TextView) findViewById(R.id.right_text);
		leftText.setText(R.string.title_activity_filex);

		listView = (ListView) findViewById(R.id.listView1);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(this);

		(btn_back = (Button) findViewById(R.id.btn_up)).setOnClickListener(this);
	}

	// 设置左右标题
	private void setTitle(String left, String right) {
		leftText.setText(left);
		editText.setText(left);
		rightText.setText(right);
	}

	// 首次搜索
	private void firstSearch() {
		mFileList.clear();

		LinuxFileCmd.ls(ROOT_DIR+mCurPath, mFileList);
		sort();
		
		setTitle(mCurPath, "" + mFileList.size());
	}

	// 文件比较器
	private final Comparator<File> comparator = new Comparator<File>() {
		// 小的在前，大的在后
		@Override
		public int compare(File lhs, File rhs) {
			if (lhs.isDirectory() && rhs.isFile()) {
				return -1;
			} else if (rhs.isDirectory() && lhs.isFile()) {
				return 1;
			}

			return 0;
		}
	};

	private void sort() {
		Collections.sort(mFileList, comparator);
	}

	// 搜索新路径
	private void search() {
		mFileList.clear();
		LinuxFileCmd.ls(ROOT_DIR+mCurPath, mFileList);

		if (mFileList.size() <= 0) {
			Toast.makeText(this, "空文件夹", Toast.LENGTH_LONG).show();
		} else {
			sort();
		}
		
		// mFileList 数据改变后，如果没有调用 notifyDataSetChanged 则会引发异常
		mAdapter.notifyDataSetChanged();
		
		setTitle(mCurPath, "" + mFileList.size());
		btn_back.setEnabled(!mCurPath.equals("/"));
	}
	
	private boolean isRootDir(){
		return mCurPath.length() == 1;
	}

	// 上级目录
	private void up() {
		if (isRootDir()) return;

		int i = mCurPath.lastIndexOf('/');
		String s = mCurPath.substring(0, i);
		i = s.lastIndexOf('/');
		mCurPath = s.substring(0, i);
		
		mCurPath += "/";
		
		//File file = new File(mCurPath);
		//String s = file.getParent();
		//if (!s.equals("/"))
		//	s += "/";

		search();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_up:
			up();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (parent.getId() == R.id.listView1) {
			File file = mFileList.get(position);

			if (file.isDirectory()) {
				mCurPath += file.getName() + "/";
				search();
			} else {
				Toast.makeText(this, "你选择了：" + file.getName(), Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(FileExActivity.this, MainActivity.class);
				intent.putExtra("mrpPath", mCurPath+file.getName());
				startActivity(intent);
			}
		}
	}

	@Override
	public void onBackPressed() {
		if (!isRootDir()) {
			up();
		} else {
			super.onBackPressed();
		}
	}

	private class FileAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mFileList.size();
		}

		@Override
		public Object getItem(int position) {
			return mFileList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHoder hoder;

			File file = mFileList.get(position);

			if (convertView == null) {
				hoder = new ViewHoder();

				LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = mInflater.inflate(R.layout.list_item1 , null);
				//if(file.isFile()){
					hoder.tv_appName = (TextView) convertView.findViewById(R.id.textView1);
					hoder.tv_fileName = (TextView) convertView.findViewById(R.id.textView2);
					hoder.tv_size = (TextView) convertView.findViewById(R.id.textView3);
				//}else {
				//	hoder.tv_fileName = (TextView) convertView.findViewById(R.id.textView2);
				//}
				hoder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
				
//				hoder.isFile = file.isFile();
				
				convertView.setTag(hoder);
			} else {
				hoder = (ViewHoder) convertView.getTag();
//				if((hoder.isFile && !file.isFile()) 
//						|| (!hoder.isFile && file.isFile())){
//					//重新创建
//					LayoutInflater mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					convertView = mInflater.inflate(file.isFile()? R.layout.list_item1 : R.layout.list_item2, null);
//					if(file.isFile()){
//						hoder.tv_appName = (TextView) convertView.findViewById(R.id.textView1);
//						hoder.tv_fileName = (TextView) convertView.findViewById(R.id.textView2);
//						hoder.tv_size = (TextView) convertView.findViewById(R.id.textView3);
//					}else {
//						hoder.tv_fileName = (TextView) convertView.findViewById(R.id.textView2);
//					}
//					hoder.icon = (ImageView) convertView.findViewById(R.id.imageView1);
//					
//					hoder.isFile = file.isFile();
//					
//					convertView.setTag(hoder);
//				}
			}

			if(file.isFile()){
				hoder.tv_fileName.setText(file.getName());
				hoder.tv_appName.setText("应用软件"+position);
				hoder.tv_size.setText(coverSize(file.length()));
			}else {
				hoder.tv_appName.setText(file.getName());
				hoder.tv_size.setText("");
				hoder.tv_fileName.setText("文件夹");
			}
			hoder.icon.setImageBitmap(file.isDirectory() ? fic_dir : fic_file);

			return convertView;
		}
		
		private String coverSize(long size) {
			String s = "";
			
			if(size < 1024)
				s += size + "b";
			else if (size < 1024*1024) {
				s = String.format("%.2f K", size/1024f);
			}else if (size < 1024*1024*1024) {
				s = String.format("%.2f M", size/1024/1024f);
			}else {
				s = String.format("%.2f G", size/1024/1024/1024f);
			}
			
			return s; 
		}

		// 保存视图
		private final class ViewHoder {
			TextView tv_fileName, tv_appName, tv_size;
			ImageView icon;
//			boolean isFile;
		}
	}
}