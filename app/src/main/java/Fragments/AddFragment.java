package Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.zhangjb.game.R;

import entity.GamePlayer;

/**
 * 添加Fragment：弹出对话框的方式实现添加
 */
public class AddFragment extends DialogFragment {

    AddFragmentListener addFragmentListener;


    //定义接口，方便与activity交互 ：
    public static interface AddFragmentListener {
        public void add(GamePlayer gamePlayer);
    }

    //获取 主页面的activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addFragmentListener = (AddFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    //保证 activity 给 fragment 传参时，旋转屏幕参数不丢失
    public static AddFragment newInstance() {
        AddFragment addFragment = new AddFragment();
        return addFragment;
    }

/************添加 fragment 实例时，会调用以下的方法**********************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /**
         *  1. 将对话框的布局实例化出来
         *  2. 创建弹出对话框
         */

        //定义一个对话框视图
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.create_game_dialog, null);

        return new AlertDialog.Builder(getActivity())
                .setIcon(android.R.drawable.ic_input_add)
                .setView(view)
                .setTitle("新增游戏玩家")
                .setPositiveButton("保存", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取界面 组件对象
                        EditText et_player = (EditText) view.findViewById(R.id.editText2_player);
                        EditText et_score = (EditText) view.findViewById(R.id.editText_cscore);
                        EditText et_level = (EditText) view.findViewById(R.id.edit_level);

                        //接收用户的输入填充对象
                        GamePlayer gamePlayer = new GamePlayer();
                        gamePlayer.setPlayer(et_player.getText().toString());
                        gamePlayer.setScore(Integer.parseInt(et_score.getText().toString()));
                        gamePlayer.setLevel(Integer.parseInt(et_level.getText().toString()));

                        addFragmentListener.add(gamePlayer);//调用activity方法 进行数据库添加操作
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();//隐藏对话框
                    }
                }).create();
    }
}
