package com.rahbarbazaar.homadit.android.utilities;

import android.widget.Filter;

import com.rahbarbazaar.homadit.android.controllers.adapters.SearchAdapter;
import com.rahbarbazaar.homadit.android.models.searchable.SearchModel;

import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter {

    SearchAdapter adapter;
    List<SearchModel> filterList;

    public CustomFilter(List<SearchModel> filterList, SearchAdapter adapter) {
        this.adapter = adapter;
        this.filterList = filterList;
    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if (constraint != null && constraint.length() > 0){

            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<SearchModel> filteredSearchModel =new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getTitle().toUpperCase().contains(constraint))// filter by title or...
                {
                    filteredSearchModel.add(filterList.get(i));
                }
            }
            results.count= filteredSearchModel.size();
            results.values= filteredSearchModel;

        }else{
            results.count=filterList.size();
            results.values=filterList;
        }
        return  results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.searchList = (ArrayList<SearchModel>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
