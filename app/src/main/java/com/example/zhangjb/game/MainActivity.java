package com.example.zhangjb.game;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import Fragments.AddFragment;
import Fragments.GamePlayFragment;
import Fragments.UpdateFragment;
import db.DatabaseAdapter;
import entity.GamePlayer;

/**
 * 程序入口
 * <p/>
 * 分析：
 * 1.有哪些功能
 * 2.在哪些界面上
 * 3.将相应的功能方法在相应的界面进行定义
 * 4.在主界面实现接口方法，用于不现界面之间的交互
 */
public class MainActivity extends AppCompatActivity implements AddFragment.AddFragmentListener, GamePlayFragment.GamePlayerFragmentListener, UpdateFragment.UpdateFragmentListener {

    //操作数据库
    private DatabaseAdapter dbAdapter;

    //要交互的 fragment
    private AddFragment addFragment; //添加数据界面
    private GamePlayFragment gamePlayFragment; //显示数据界面
    private UpdateFragment updateFragment; //更新数据界面


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAdapter = new DatabaseAdapter(this);//实例化操作数据库类类

        //显示列表数据
        showGamePlayerFragment();
    }

    /**
     * 实例化菜单 布局
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * 当点击菜单项按钮时触发
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add://添加按钮的操作
                addFragment = AddFragment.newInstance();//显示添加界面
                addFragment.show(getFragmentManager(), null);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //添加
    @Override
    public void add(GamePlayer gamePlayer) {
        dbAdapter.add(gamePlayer);
        gamePlayFragment.changeData();//添加后，重新加载数据
    }

    //显示列表
    @Override
    public void showGamePlayerFragment() {

        //获取 显示数据列表的fragment对象
        gamePlayFragment = GamePlayFragment.newInstance();// 会调用:onCreateView方法

        //开启事务
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        //将 activity 中的布局文件 替换成 fragment
        ft.replace(R.id.main_layout, gamePlayFragment);

        ft.addToBackStack(null);
        ft.commit();

    }

    //显示更新fragment视图
    @Override
    public void showUpdateFragment(int id) {
        updateFragment = UpdateFragment.newInstance(id);

        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.replace(R.id.main_layout, updateFragment);

        ft.addToBackStack(null);

        ft.commit();
    }

    //删除
    @Override
    public void delete(int id) {
        dbAdapter.delete(id);
        gamePlayFragment.changeData();//删除后 重新加载数据

    }

    //查找所有
    @Override
    public ArrayList<GamePlayer> findAll() {
        return dbAdapter.findAll();
    }

    //查找一个
    @Override
    public GamePlayer findById(int id) {
        return dbAdapter.findById(id);
    }

    //更新数据
    @Override
    public void update(GamePlayer gamePlayer) {
        dbAdapter.update(gamePlayer);
        gamePlayFragment.changeData();//更新后 重新加载数据

    }


    /****************手机按键 监听事件*******************************/
    /**
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if(getFragmentManager().getBackStackEntryCount()==1){//当 栈中只是一个fragment时，就结束程序
                finish();
            }else {
                getFragmentManager().popBackStack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
