package com.vargancys.vargancysplayer.module.search.data;

import java.util.List;

/**
 * author: Vagrancy
 * e-mail: 18050829067@163.com
 * time  : 2020/02/11
 * version:1.0
 */
public class SearchNetInfo {
    private String flag;
    private String pageNo;
    private String pageSize;
    private String wd;
    private String total;
    private List<ItemsEntity> items;

    public List<ItemsEntity> getItems() {
        return items;
    }

    public void setItems(List<ItemsEntity> items) {
        this.items = items;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPageNo() {
        return pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }

    public static class ItemsEntity{
        private String itemId;
        private String itemTitle;
        private String itemType;
        private String detailUri;
        private String pubTime;
        private String keywords;
        private String category;
        private String guid;
        private String videoLength;
        private String source;
        private String brief;
        private String photoCount;
        private String sub_column_id;
        private String datecheck;
        private List<ItemImageEntity> itemImages;

        public List<ItemImageEntity> getItemImages() {
            return itemImages;
        }

        public void setBrief(String brief) {
            this.brief = brief;
        }

        public String getBrief() {
            return brief;
        }

        public String getCategory() {
            return category;
        }


        public String getDatecheck() {
            return datecheck;
        }

        public String getDetailUri() {
            return detailUri;
        }

        public String getGuid() {
            return guid;
        }

        public String getItemId() {
            return itemId;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public String getItemType() {
            return itemType;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getPhotoCount() {
            return photoCount;
        }

        public String getPubTime() {
            return pubTime;
        }

        public String getSource() {
            return source;
        }

        public String getSub_column_id() {
            return sub_column_id;
        }

        public String getVideoLength() {
            return videoLength;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setDatecheck(String datecheck) {
            this.datecheck = datecheck;
        }

        public void setDetailUri(String detailUri) {
            this.detailUri = detailUri;
        }

        public void setGuid(String guid) {
            this.guid = guid;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public void setItemImages(List<ItemImageEntity> itemImages) {
            this.itemImages = itemImages;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public void setItemType(String itemType) {
            this.itemType = itemType;
        }

        public void setKeywords(String keywords) {
            this.keywords = keywords;
        }

        public void setPhotoCount(String photoCount) {
            this.photoCount = photoCount;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setSub_column_id(String sub_column_id) {
            this.sub_column_id = sub_column_id;
        }

        public void setVideoLength(String videoLength) {
            this.videoLength = videoLength;
        }

        public static class ItemImageEntity{
            private String itemUri;

            public String getItemUri() {
                return itemUri;
            }

            public void setItemUri(String itemUri) {
                this.itemUri = itemUri;
            }
        }
    }

}
