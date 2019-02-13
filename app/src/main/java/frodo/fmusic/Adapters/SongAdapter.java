package frodo.fmusic.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import frodo.fmusic.Activities.MainActivity;
import frodo.fmusic.Code.Manager;
import frodo.fmusic.Interfaces.OnItemClicked;
import frodo.fmusic.Modelos.Song;
import frodo.fmusic.R;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements FastScrollRecyclerView.SectionedAdapter {

    private OnItemClicked onClick;
    private Manager manager;
    private Song song;

    public SongAdapter(){
        manager = MainActivity.getManager();
    }

    @Override
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.personalizado_list_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        song = manager.getSong(position);
        holder.bind(song, onClick);
    }

    @Override
    public int getItemCount() {
        return manager.getSizeList();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }

    @NonNull
    @Override
    public String getSectionName(int position) {
        return String.format("%c",manager.getSong(position).getName().charAt(0));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_view_titulo) TextView title;
        @BindView(R.id.text_view_artista) TextView artist;
        @BindView(R.id.image_view_caratula_lista) ImageView caratula;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Song song, final OnItemClicked listener){

            title.setText(song.getName());
            artist.setText(song.getArtist().getName());
            Glide.with(itemView)
                    .load(song.getAlbum().getUriAlbumArt())
                    .apply(new RequestOptions()
                    .error(R.drawable.ic_image_caratula_null)
                    .centerCrop())
                    .into(caratula);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}

