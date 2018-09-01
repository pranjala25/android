package books.com.shelfshare.config;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import books.com.shelfshare.AllBooks;
import books.com.shelfshare.MyBooks;
import books.com.shelfshare.R;
import books.com.shelfshare.SingleBookInfo;


/**
 * Created by ppranjal on 12/16/2016.
 */
public class BookAdapter extends RecyclerView.Adapter<BookAdapter.CustomViewHolder>  {


    private List<Book> bookList;


    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, author, condition;

        public CustomViewHolder(View view){
            super(view);
            title = (TextView) view.findViewById(R.id.list_title);
            author = (TextView) view.findViewById(R.id.list_author);
            condition = (TextView) view.findViewById(R.id.list_cond);
        }

        @Override
        public void onClick(View view) {
            int i = getAdapterPosition();
            Context context = view.getContext();
            Intent intent = new Intent(context,SingleBookInfo.class);
            intent.putExtra("title",bookList.get(i).getTitle());
            intent.putExtra("author",bookList.get(i).getAuthor());
            intent.putExtra("desc",bookList.get(i).getGenre());
            intent.putExtra("condition",bookList.get(i).getCondition());
            intent.putExtra("uid",bookList.get(i).getUserId());
            intent.putExtra("rented",bookList.get(i).isRented());
            context.startActivity(intent);
            // String id = getIntent().getExtras().getString("uid");


        }
    }

    public BookAdapter(List<Book> bookList){
        this.bookList = bookList;
    }
    @Override
    public BookAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_item,parent,false);
        return new CustomViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BookAdapter.CustomViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.condition.setText(book.getCondition());

    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }
}
