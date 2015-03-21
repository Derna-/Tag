package teamone.tag;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by nadergeorgi on 20/02/15.
 */
public class MyAdapter  extends  RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private LayoutInflater inflater;
    List<Information> data = Collections.emptyList();
    private Context context;
    private ClickListener clickListener;

    public MyAdapter(Context context, List<Information> data) {
        inflater = LayoutInflater.from(context);
        this.data=data;
        this.context=context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_row, parent, false);
        Log.d("_LOG_", "onCreateHolder called ");
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current = data.get(position);
        Log.d("_LOG_", "onBindViewHolder called " + position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconId);

    }

    public void setClickListener(ClickListener clickListener){
        this.clickListener=clickListener;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon= (ImageView) itemView.findViewById(R.id.listIcon);
            icon.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //DEBUG :
            // context.startActivity(new Intent(context,Developpeurs.class));
            // Toast.makeText(context, "Itemclicked at " + getPosition(), Toast.LENGTH_SHORT).show();
            if( clickListener != null)
            {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener{
        public void itemClicked(View view , int position);
    }
}
