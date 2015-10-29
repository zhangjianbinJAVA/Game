package Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.zhangjb.game.R;

import entity.GamePlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment implements View.OnClickListener {

    private EditText et_player;
    private EditText et_score;
    private EditText et_level;

    private GamePlayer gamePlayer;

    //activity 实现的接口
    UpdateFragmentListener updateFragmentListener;


    //定义接口
    public static interface UpdateFragmentListener {
        public GamePlayer findById(int id); //作用：查询

        public void update(GamePlayer gamePlayer);//作用：更新
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            updateFragmentListener = (UpdateFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    //保证 activity 给 fragment 传参时，旋转屏幕参数不丢失
    public static UpdateFragment newInstance(int id) {
        UpdateFragment updateFragment = new UpdateFragment();
        Bundle b = new Bundle();
        b.putInt("id", id);

        updateFragment.setArguments(b);// //保存传入的参数
        return updateFragment;
    }

    /*******************实例化 fragment 时，调用以下方法****************************/
    /**
     * 实例化时 查询数据
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int id = getArguments().getInt("id");

        //activity 从数据库中查询指定id的对象数据
        gamePlayer = updateFragmentListener.findById(id);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //实例化 fragment布局
        View view = inflater.inflate(R.layout.fragment_update, container, false);

        //查找界面 组件
        TextView tv_id = (TextView) view.findViewById(R.id.textView2_id);

        et_player = (EditText) view.findViewById(R.id.editText2_player);
        et_score = (EditText) view.findViewById(R.id.editText2_score);
        et_level = (EditText) view.findViewById(R.id.editText2_level);

        //界面按钮
        Button button_save = (Button) view.findViewById(R.id.button_save);
        Button button_cancel = (Button) view.findViewById(R.id.button_cancel);

        //注册监听事件
        button_save.setOnClickListener(this);
        button_cancel.setOnClickListener(this);

        //组件内容填充
        tv_id.setText(String.valueOf(gamePlayer.getId()));
        et_player.setText(gamePlayer.getPlayer());
        et_score.setText(String.valueOf(gamePlayer.getScore()));
        et_level.setText(String.valueOf(gamePlayer.getLevel()));

        return view;
    }


    /**************** fragment 上按钮的监听事件 *************************/
    /**
     * 按钮 监听事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                save();
                break;
            case R.id.button_cancel:
                getActivity().getFragmentManager().popBackStack();//弹出 栈
                break;
        }

    }

    //数据提交到数据库
    private void save() {
        GamePlayer g = new GamePlayer();

        //组件的输入内容添加 到数据库中
        g.setId(gamePlayer.getId());
        g.setPlayer(et_player.getText().toString());
        g.setScore(Integer.parseInt(et_score.getText().toString()));
        g.setLevel(Integer.parseInt(et_level.getText().toString()));

        //调用 activity中的更新方法
        updateFragmentListener.update(g);
        getActivity().getFragmentManager().popBackStack(); //出栈
    }

    @Override
    public void onDetach() {
        super.onDetach();
        updateFragmentListener = null;
    }
}
