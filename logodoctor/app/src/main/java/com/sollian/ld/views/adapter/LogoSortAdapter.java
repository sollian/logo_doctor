package com.sollian.ld.views.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.sollian.ld.R;
import com.sollian.ld.models.Logo;
import com.sollian.ld.utils.pinyinutils.CharacterParser;
import com.sollian.ld.utils.pinyinutils.PinyinComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import smartimageview.SmartImageView;

/**
 * Created by sollian on 2015/9/18.
 */
public class LogoSortAdapter extends AbsBaseAdapter<Logo> implements SectionIndexer, Filterable {
    private Filter filter;
    private CharacterParser characterParser;

    public LogoSortAdapter(Context context, List<Logo> dataSource) {
        super(context, dataSource);
        characterParser = CharacterParser.getInstance();
        sort();
    }

    private void sort() {
        for(Logo logo : dataSource) {
            if(TextUtils.isEmpty(logo.getSortLetters())) {
                // 汉字转换成拼音
                String pinyin = characterParser.getSelling(logo.getName());
                String sortString = pinyin.substring(0, 1).toUpperCase();

                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]+")) {
                    logo.setSortLetters(sortString.toUpperCase());
                } else {
                    logo.setSortLetters("#");
                }
            }
        }
        if (dataSource != null && !dataSource.isEmpty()) {
            Collections.sort(dataSource, new PinyinComparator());
        }
    }

    @Override
    public void notifyDataSetChanged() {
        sort();
        super.notifyDataSetChanged();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_logo;
    }

    @Override
    protected IHolder getHolder() {
        return new Holder();
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return getItem(position).getSortedLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getItem(i).getSortedLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new LogoFilter(dataSource);
        }
        return filter;
    }

    private class LogoFilter extends Filter {
        private List<Logo> originDataSource;
        private List<Logo> dataSource;

        public LogoFilter(List<Logo> dataSource) {
            this.dataSource = dataSource;
            originDataSource = new ArrayList<>();
            originDataSource.addAll(dataSource);
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            List<Logo> newSource;

            if (TextUtils.isEmpty(constraint)) {
                newSource = originDataSource;
            } else {
                newSource = new ArrayList<>();
                for (Logo logo : originDataSource) {
                    if (logo.getName().contains(constraint)
                        || logo.getSortedLetters().contains(constraint)
                        || (logo.getExtra() != null && logo.getExtra().contains(constraint))) {
                        newSource.add(logo);
                    }
                }
            }
            results.count = newSource.size();
            results.values = newSource;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            dataSource.clear();
            dataSource.addAll(((List<Logo>) results.values));
            notifyDataSetChanged();
        }
    }

    private class Holder implements IHolder {
        public SmartImageView sivImg;
        public TextView tvName;
        public TextView tvExtra;
        public TextView tvHead;

        @Override
        public void onBindView(View convertView) {
            sivImg = (SmartImageView) convertView.findViewById(R.id.img);
            tvName = (TextView) convertView.findViewById(R.id.name);
            tvExtra = (TextView) convertView.findViewById(R.id.extra);
            tvHead = (TextView) convertView.findViewById(R.id.head);
        }

        @Override
        public void onProcessView(int position, View convertView) {
            Logo item = getItem(position);

            if (!TextUtils.isEmpty(item.getWrappedImg())) {
                sivImg.setImageUrl(item.getWrappedImg(), R.drawable.ic_launcher, R.drawable.ic_launcher);
            } else {
                sivImg.setImageResource(R.drawable.ic_launcher);
            }

            tvName.setText(item.getName());

            if (!TextUtils.isEmpty(item.getExtra())) {
                tvExtra.setText(item.getExtra());
            } else {
                tvExtra.setText("");
            }

            // 根据position获取分类的首字母的Char ascii值
            int section = getSectionForPosition(position);

            // 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                tvHead.setVisibility(View.VISIBLE);
                tvHead.setText(item.getSortedLetters());
            } else {
                tvHead.setVisibility(View.GONE);
            }
        }
    }
}
