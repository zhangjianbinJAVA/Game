package Fragments;


import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.zhangjb.game.R;

import java.util.ArrayList;

import entity.GamePlayer;

/**
 * A simple {@link Fragment} subclass.
 */
public class GamePlayFragment extends Fragment {

    //适配器对象
    GamePlayerAdapter gamePlayerAdapter;

    //接口对象
    GamePlayerFragmentListener gamePlayerFragmentListener;

    //定义接口
    public static interface GamePlayerFragmentListener {
        public void showGamePlayerFragment();//作用：显示列表 界面

        public void showUpdateFragment(int id);//作用：更新数据的  从当前页面到更新页面，需要将本页的id带到更新页面上去 界面

        public void delete(int id);

        public ArrayList<GamePlayer> findAll();
    }


    //获取 主页面的activity
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            gamePlayerFragmentListener = (GamePlayerFragmentListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }


    //保证 activity 给 fragment 传参时，旋转屏幕参数不丢失
    public static GamePlayFragment newInstance() {
        GamePlayFragment gamePlayFragment = new GamePlayFragment();
        return gamePlayFragment;
    }

    /************************显示列表 fragment 实例会调以下方法*************************/
    /**
     * 在这里实例化 适配器对象,并填充数据
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //从数据库中获取列表数据
        ArrayList<GamePlayer> gamePlayers = gamePlayerFragmentListener.findAll();

        //实例化 适配器
        gamePlayerAdapter = new GamePlayerAdapter(getActivity(), gamePlayers);
    }


    /**
     * 自定义 listView布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //实例化 fragment布局
        View view = inflater.inflate(R.layout.fragment_game_play, container, false);

        ListView listView = (ListView) view.findViewById(R.id.listView);

        //注册上下文，因为长按时，要弹出菜单
        registerForContextMenu(listView);

        //        自定义布局
        listView.setAdapter(gamePlayerAdapter);//设置 适配器
        return view;
    }


    //自定义适配器
    private static class GamePlayerAdapter extends BaseAdapter {

        private Context context;

        //列表 数据
        private ArrayList<GamePlayer> gamePlayers;

        public GamePlayerAdapter(Context context, ArrayList<GamePlayer> gamePlayers) {
            this.context = context;
            this.gamePlayers = gamePlayers;
        }

        //重新设置
        public void setGamePlayers(ArrayList<GamePlayer> gamePlayers) {
            this.gamePlayers = gamePlayers;
        }

        @Override
        public int getCount() {
            return gamePlayers.size();
        }

        @Override
        public Object getItem(int position) {
            return gamePlayers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //节省性能,使用ViewHolder减少对象的查找
            ViewHolder vh = null;

            if (convertView == null) {//convertView:缓存item  减少对象的创建
                //实例化 item 布局
                LayoutInflater.from(context).inflate(R.layout.game_player_list_item_layout, null);

                vh = new ViewHolder();
                vh.tv_id = (TextView) convertView.findViewById(R.id.textView1_id);
                vh.tv_player = (TextView) convertView.findViewById(R.id.textView1_player);
                vh.tv_score = (TextView) convertView.findViewById(R.id.textView_score);
                vh.tv_level = (TextView) convertView.findViewById(R.id.textView_level);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            //组件数据填充
            GamePlayer g = gamePlayers.get(position);
            vh.tv_id.setText(String.valueOf(g.getId()));
            vh.tv_player.setText(g.getPlayer());
            vh.tv_score.setText(g.getScore());
            vh.tv_level.setText(g.getLevel());

            return convertView;
        }
    }


    //用于保存每一次查找的组件，避免下次重复查找
    private static class ViewHolder {
        TextView tv_id;
        TextView tv_player;
        TextView tv_score;
        TextView tv_level;
    }


    /**************长按 每个item 时弹出菜单 ***********************************/
    /**
     * 创建 菜单项
     *
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("修改/删除");//菜单标题
        menu.setHeaderIcon(android.R.drawable.ic_menu_edit);

        //实例化 菜单项
        getActivity().getMenuInflater().inflate(R.menu.listview_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu:
                //获取菜单 信息对象
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                //获取前当前item的指定组件，  targetView ：当前点击的item视图
                TextView textView_id = (TextView) info.targetView.findViewById(R.id.textView1_id);

                //获取组件上的内容，即id
                int id = Integer.parseInt(textView_id.getText().toString());

                //调用 activity的删除方法，删除指定的 id 数据
                gamePlayerFragmentListener.delete(id);

                //重新查询视图列表数据
                changeData();
                break;
            case R.id.update_menu:
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                textView_id = (TextView) info.targetView.findViewById(R.id.textView1_id);

                id = Integer.parseInt(textView_id.getText().toString());

                //调用 activity的更新方法，显示更新的视图
                gamePlayerFragmentListener.showUpdateFragment(id);
                break;
        }
        return super.onContextItemSelected(item);
    }

    //重新查询数据
    public void changeData() {
        //从数据库查询数据，并填充到适配器中
        gamePlayerAdapter.setGamePlayers(gamePlayerFragmentListener.findAll());

        //让适配器重新加载数据 ，通知视内容更变
        gamePlayerAdapter.notifyDataSetChanged();

    }


    /**
     * 销毁时，置空
     */
    @Override
    public void onDetach() {
        super.onDetach();
        gamePlayerFragmentListener = null;
    }
}
