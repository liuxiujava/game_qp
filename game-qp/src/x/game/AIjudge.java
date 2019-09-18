/*
package x.game;

public class AIjudge {

    private int five, alive_4, highdie_4, die_4, alive_3, die_3, alive_2, die_2, lowalive_2, tiaoalive_3;

    public void Init() {
        five = 0;
        alive_4 = 0;
        highdie_4 = 0;
        die_4 = 0;
        alive_3 = 0;
        die_3 = 0;
        alive_2 = 0;
        die_2 = 0;
        lowalive_2 = 0;
        tiaoalive_3 = 0;
    }

    public Point getright(int[][] chesses, int x, int y) {// 右
        Point count = new Point();
        for (int i = x + 1; i < Config.columns; i++) {
            if (chesses[i][y] != chesses[x][y]) {
                count = new Point(i, y);
                // System.out.println("右："+count);
                return count;
            }
        }
        return count;
    }

    public Point getleft(int[][] chesses, int x, int y) {// 左
        Point count = new Point();
        for (int i = x - 1; i >= 0; i--) {
            if (chesses[i][y] != chesses[x][y]) {
                count = new Point(i, y);
                return count;
            }
        }
        return count;
    }

    public Point getup(int[][] chesses, int x, int y) {// 上
        Point count = new Point();
        for (int i = y; i >= 0; i--) {
            if (chesses[x][i] != chesses[x][y]) {
                count = new Point(x, i);
                return count;
            }
        }
        return count;
    }

    public Point getdown(int[][] chesses, int x, int y) {// 下
        Point count = new Point();
        for (int i = y + 1; i < Config.rows; i++) {
            if (chesses[x][i] != chesses[x][y]) {
                count = new Point(x, i);
                return count;
            }
        }
        return count;
    }

    public Point getSlashru(int[][] chesses, int x, int y) {// 右上
        Point count = new Point();
        for (int i = y - 1, j = x + 1; j < Config.rows && i >= 0; j++, i--) {
            if (chesses[j][i] != chesses[x][y]) {
                count = new Point(j, i);
                return count;
            }
        }
        return count;
    }

    public Point getSlashrd(int[][] chesses, int x, int y) {// 右下
        Point count = new Point();
        for (int i = y + 1, j = x + 1; j < Config.rows && i < Config.columns; j++, i++) {
            if (chesses[j][i] != chesses[x][y]) {
                count = new Point(j, i);
                return count;
            }
        }
        return count;
    }

    public Point getSlashlu(int[][] chesses, int x, int y) {// 左上
        Point count = new Point();
        for (int i = y, j = x; j >= 0 && i >= 0; j--, i--) {
            if (chesses[j][i] != chesses[x][y]) {
                count = new Point(j, i);
                return count;
            }
        }
        return count;
    }

    public Point getSlashld(int[][] chesses, int x, int y) {// 左下
        Point count = new Point();
        for (int i = y + 1, j = x - 1; j >= 0 && i < Config.columns; j--, i++) {
            if (chesses[j][i] != chesses[x][y]) {
                count = new Point(j, i);
                return count;
            }
        }
        return count;
    }

    public int judge(int[][] chesses, int x, int y, int data) {
        this.Init();
        chesses[x][y] = data;// 给个假设值！
        Point index_r = getright(chesses, x, y);// 右
        Point index_l = getleft(chesses, x, y);// 左
        Point index_u = getup(chesses, x, y);// 上
        Point index_d = getdown(chesses, x, y);// 下
        Point index_ru = getSlashru(chesses, x, y);// 右上
        Point index_rd = getSlashrd(chesses, x, y);// 右下
        Point index_lu = getSlashlu(chesses, x, y);// 左上
        Point index_ld = getSlashld(chesses, x, y);// 左下
        int left = chesses[index_l.x][index_l.y];// 左
        int left1 = 9, right1 = 9, left2 = 9, right2 = 9, left3 = 9, right3 = 9;
        if (index_l.x > 0)
            left1 = chesses[index_l.x - 1][index_l.y];
        int right = chesses[index_r.x][index_r.y];// 右
        if (index_r.x < Config.columns - 2)
            right1 = chesses[index_r.x + 1][index_r.y];
        if (index_l.x - 1 > 0)
            left2 = chesses[index_l.x - 2][index_l.y];
        if (index_r.x < Config.columns - 3)
            right2 = chesses[index_r.x + 2][index_r.y];
        if (index_l.x - 2 > 0)
            left3 = chesses[index_l.x - 3][index_l.y];
        if (index_r.x < Config.columns - 4)
            right3 = chesses[index_r.x + 3][index_r.y];

        // 纵向
        int up = chesses[index_u.x][index_u.y];// 上
        int up1 = 9, down1 = 9, up2 = 9, down2 = 9, up3 = 9, down3 = 9;
        if (index_u.x > 0)
            up1 = chesses[index_u.x - 1][index_u.y];
        int down = chesses[index_d.x][index_d.y];// 1下
        if (index_d.x < Config.rows - 2)
            down1 = chesses[index_d.x + 1][index_d.y];
        if (index_u.x - 1 > 0)
            up2 = chesses[index_u.x - 2][index_u.y];
        if (index_d.x < Config.rows - 3)
            down2 = chesses[index_d.x + 2][index_d.y];
        if (index_u.x - 2 > 0)
            up3 = chesses[index_u.x - 3][index_u.y];
        if (index_d.x < Config.rows - 4)
            down3 = chesses[index_d.x + 3][index_d.y];

        // 左斜线

        int ld = chesses[index_ld.x][index_ld.y];// 左下
        int ld1 = 9, ru1 = 9, ld2 = 9, ru2 = 9, ld3 = 9, ru3 = 9;
        if (index_ld.x > 0 && index_ld.y < Config.rows - 2)// 1左下
            ld1 = chesses[index_ld.x - 1][index_ld.y + 1];
        int ru = chesses[index_ru.x][index_ru.y];// 右上
        if (index_ru.x < Config.columns - 2 && index_ru.y > 0)// 右上
            ru1 = chesses[index_ru.x + 1][index_ru.y - 1];
        if (index_ld.x - 1 > 0 && index_ld.y < Config.rows - 3)// 2左下
            ld2 = chesses[index_ld.x - 2][index_ld.y + 2];
        if (index_ru.x < Config.columns - 3 && index_ru.y > 1)// 右上
            ru2 = chesses[index_ru.x + 2][index_ru.y - 2];
        if (index_ld.x - 2 > 0 && index_ld.y < Config.rows - 4)// 3左下
            ld2 = chesses[index_ld.x - 3][index_ld.y + 3];
        if (index_ru.x < Config.columns - 4 && index_ru.y > 2)// 右上
            ru2 = chesses[index_ru.x + 3][index_ru.y - 3];

        // 右斜线
        int lu = chesses[index_lu.x][index_lu.y];// 右上
        int lu1 = 9, rd1 = 9, lu2 = 9, rd2 = 9, lu3 = 9, rd3 = 9;
        if (index_lu.x > 0 && index_lu.y > 0)// 1左上
            lu1 = chesses[index_lu.x - 1][index_lu.y - 1];
        int rd = chesses[index_rd.x][index_rd.y];// 左下
        if (index_rd.x < Config.columns - 2 && index_rd.y < Config.rows - 2)// 右下
            rd1 = chesses[index_rd.x + 1][index_rd.y + 1];
        if (index_lu.x - 1 > 0 && index_lu.y > 1)// 2左上
            lu2 = chesses[index_lu.x - 2][index_lu.y - 2];
        if (index_rd.x < Config.columns - 3 && index_rd.y < Config.rows - 3)// 右下
            rd2 = chesses[index_rd.x + 2][index_rd.y + 2];
        if (index_lu.x - 2 > 0 && index_lu.y > 2)// 3左上
            lu2 = chesses[index_lu.x - 3][index_lu.y - 3];
        if (index_rd.x < Config.columns - 4 && index_rd.y < Config.rows - 4)// 右下
            rd2 = chesses[index_rd.x + 3][index_rd.y + 3];
        */
/**
         * 五连珠
         *//*


        if (Check.checkfive(chesses, x, y))// 成5
        {
            five++;
        }
        */
/**
         * 四连珠
         *//*

        if (Check.checkRow(chesses, x, y) == 4) {// 横向四连珠
            System.out.println("四连珠 <" + x + ", " + y + ">");
            if (left == 0 && right == 0)// 活四
            {
                alive_4++;
            } else if ((left == 0 && right == -data) || (left == -data && right == 0)) {
                highdie_4++;
            } // 死4
        }
        if (Check.checkColumn(chesses, x, y) == 4) {// 纵向四连珠
            if (up == 0 && down == 0)// 活四
            {
                alive_4++;
            } else if ((up == 0 && down == -data) || (up == -data && down == 0)) {
                highdie_4++;
            } // 眠4
        }
        if (Check.checkIncleft(chesses, x, y) == 4) {// 左斜线：左下右上
            if (ld == 0 && ru == 0)// 活四
            {
                alive_4++;
            } else if ((ld == 0 && ru == -data) || (ld == -data && ru == 0)) {
                highdie_4++;
            } // 眠4
        }
        if (Check.checkIncright(chesses, x, y) == 4) {// 右斜线：左上右下
            if (lu == 0 && rd == 0)// 活四
            {
                alive_4++;
            } else if ((lu == 0 && rd == -data) || (lu == -data && rd == 0)) {
                highdie_4++;
            } // 眠4
        }
        */
/**
         * 三连珠
         *//*

        if (Check.checkRow(chesses, x, y) == 3) {// 横向三连珠
            if (left == 0 && right == 0) {// 断开处空
                if (left1 == 0 || right1 == 0)// 活三
                {
                    alive_3++;
                }
                if (left1 == data || right == data)// 死4
                {
                    die_4++;
                }
                if (left1 == -data || right1 == -data)// 眠三
                {
                    die_3++;
                }
            } else if (left == -data && right == 0) {// 左边被拦
                if (right1 == data)// 右边隔一个有棋子，眠四
                {
                    die_4++;
                } else if (right1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            } else if (right == -data && left == 0) {// 右边被拦
                if (left1 == data)// 左边隔一个棋子，眠四
                {
                    die_4++;
                } else if (left1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            }

        }
        if (Check.checkColumn(chesses, x, y) == 3) {// 纵向三连珠
            if (up == 0 && down == 0) {// 断开处空
                if (up1 == 0 || down1 == 0)// 活三
                {
                    alive_3++;
                }
                if (up1 == data || down == data)// 死4
                {
                    die_4++;
                }
                if (up1 == -data || down1 == -data)// 眠三
                {
                    die_3++;
                }
            } else if (up == -data && down == 0) {// 左边被拦
                if (down1 == data)// 右边隔一个有棋子，眠四
                {
                    die_4++;
                } else if (down1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            } else if (down == -data && up == 0) {// 右边被拦
                if (up1 == data)// 左边隔一个棋子，眠四
                {
                    die_4++;
                } else if (up1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            }

        }
        if (Check.checkIncleft(chesses, x, y) == 3) {// 左斜线三连珠 ：左下+右上
            if (ld == 0 && ru == 0) {// 断开处空
                if (ld1 == 0 || ru1 == 0)// 活三
                {
                    alive_3++;
                }
                if (ld1 == data || ru == data)// 死4
                {
                    die_4++;
                }
                if (ld1 == -data || ru1 == -data)// 眠三
                {
                    die_3++;
                }
            } else if (ld == -data && ru == 0) {// 左边被拦
                if (ru1 == data)// 右边隔一个有棋子，眠四
                {
                    die_4++;
                } else if (ru1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            } else if (ru == -data && ld == 0) {// 右边被拦
                if (ld1 == data)// 左边隔一个棋子，眠四
                {
                    die_4++;
                } else if (ld1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            }

        }
        if (Check.checkIncright(chesses, x, y) == 3) {// 右斜三连珠：右下+左上
            if (lu == 0 && rd == 0) {// 断开处空
                if (lu1 == 0 || rd1 == 0)// 活三
                {
                    System.out.println(data + "<" + x + "," + y + ">右斜线活3   右斜线检查" + Check.checkIncright(chesses, x, y)
                            + "左斜线检查：" + Check.checkIncleft(chesses, x, y));
                    System.out.println("<" + x + "," + y + ">" + "" + lu3 + "" + "live2：" + lu2 + " live3：" + lu1
                            + " die3：" + lu + " low2：" + rd);
                    alive_3++;
                }
                if (lu1 == data || rd == data)// 死4
                {
                    die_4++;
                }
                if (lu1 == -data || rd1 == -data)// 眠三
                {
                    die_3++;
                }
            }
            if (lu == -data && rd == 0) {// 左边被拦
                if (rd1 == data)// 右边隔一个有棋子，眠四
                {
                    die_4++;
                } else if (rd1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            }
            if (rd == -data && lu == 0) {// 右边被拦
                if (lu1 == data)// 左边隔一个棋子，眠四
                {
                    die_4++;
                } else if (lu1 == 0)// 没有放棋子为眠3
                {
                    die_3++;
                }
            }

        }
        */
/**
         * 二连珠
         *//*

        if (Check.checkRow(chesses, x, y) == 2) {// 横向二连珠

            if (right == 0 && left == 0) {// 如果两边断开位置都为空
                // System.out.println("二连珠"+left2+" "+left1+" "+left+" "+right+"
                // "+right1+" "+right2);
                if ((right1 == 0 && right2 == data) || (left1 == 0 && left2 == data))// 第三棋子有一个为本色，眠3
                {
                    die_3++;
                } else if (right1 == 0 && left1 == 0)// 断开第二个位置都为空，活2
                {
                    // System.out.println("活二！");
                    alive_2++;
                }
                if ((right1 == data && right2 == 0) || (left1 == data && left2 == 0))
                // 某一边断开第二个位置为本色，第三位置为空，跳活三
                {
                    tiaoalive_3++;
                }
                if ((right1 == data && right2 == -data) || (left1 == data && left2 == -data))
                // 某一边断开第二位置为本色，得三位置为他色，眠三
                {
                    die_3++;
                }
                if ((right1 == data && right2 == data) || (left1 == data && left2 == data))
                // 某一边的12都为本色，眠四
                {
                    die_4++;
                }
            } else if (right == 0 && left == -data) {// 右边为空左边被拦
                if (right1 == data && right2 == data)// 右边12都为本色，眠四
                {
                    die_4++;
                } else if (right1 == data || right2 == data)// 右2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (right1 == 0 && right2 == 0)// 右123都为空,眠2
                {
                    die_2++;
                }

            } else if (left == 0 && right == -data) {// 左边为空右边被拦
                if (left1 == data && left2 == data)// 左边12都为本色，眠四
                {
                    die_4++;
                } else if (left1 == data || left2 == data)// 左2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (left1 == 0 && left2 == 0)// 左123为空，眠2
                {
                    die_2++;
                }
            }
        }
        if (Check.checkColumn(chesses, x, y) == 2) {// 纵向二连珠

            if (up == 0 && down == 0) {// 如果两边断开位置都为空
                if ((up1 == 0 && up2 == data) || (down1 == 0 && down2 == data))// 第三棋子有一个为本色，眠3
                {
                    die_3++;
                } else if (up1 == 0 && down1 == 0)// 断开第二个位置都为空，活2
                {
                    alive_2++;
                }
                if ((up1 == data && up2 == 0) || (down1 == data && down2 == 0))
                // 某一边断开第二个位置为本色，第三位置为空，跳活三
                {
                    tiaoalive_3++;
                }
                if ((up1 == data && up2 == -data) || (down1 == data && down2 == -data))
                // 某一边断开第二位置为本色，得三位置为他色，眠三
                {
                    die_3++;
                }
                if ((up1 == data && up2 == data) || (down1 == data && down2 == data))
                // 某一边的12都为本色，眠四
                {
                    die_4++;
                }
            } else if (up == 0 && down == -data) {// 上边为空下边被拦
                if (up1 == data && up2 == data)// 上边12都为本色，眠四
                {
                    die_4++;
                } else if (up1 == data || up2 == data)// 上2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (up1 == 0 && up2 == 0)// 上123都为空,眠2
                {
                    die_2++;
                }

            } else if (down == 0 && up == -data) {// 下边为空上边被拦
                if (down1 == data && down2 == data)// 下边12都为本色，眠四
                {
                    die_4++;
                } else if (down1 == data || down2 == data)// 下2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (down1 == 0 && down2 == 0)// 下123为空，眠2
                {
                    die_2++;
                }
            }

        }
        if (Check.checkIncleft(chesses, x, y) == 2) {// 左斜线：左下ld+右上ru
            if (ld == 0 && ru == 0) {// 如果两边断开位置都为空
                if ((ld1 == 0 && ld2 == data) || (ru1 == 0 && ru2 == data))// 第三棋子有一个为本色，眠3
                {
                    die_3++;
                } else if (ld1 == 0 && ru1 == 0)// 断开第二个位置都为空，活2
                {
                    alive_2++;
                }
                if ((ld1 == data && ld2 == 0) || (ru1 == data && ru2 == 0))
                // 某一边断开第二个位置为本色，第三位置为空，跳活三
                {
                    tiaoalive_3++;
                }
                if ((ld1 == data && ld2 == -data) || (ru1 == data && ru2 == -data))
                // 某一边断开第二位置为本色，得三位置为他色，眠三
                {
                    die_3++;
                }
                if ((ld1 == data && ld2 == data) || (ru1 == data && ru2 == data))
                // 某一边的12都为本色，眠四
                {
                    die_4++;
                }
            } else if (ld == 0 && ru == -data) {// 左边为空右边被拦
                if (ld1 == data && ld2 == data)// 左边12都为本色，眠四
                {
                    die_4++;
                } else if (ld1 == data || ld2 == data)// 左2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (ld1 == 0 && ld2 == 0)// 左123都为空,眠2
                {
                    die_2++;
                }

            } else if (ru == 0 && ld == -data) {// 右边为空右边被拦
                if (ru1 == data && ru2 == data)// 右边12都为本色，眠四
                {
                    die_4++;
                } else if (ru1 == data || ru2 == data)// 右2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (ru1 == 0 && ru2 == 0)// 右123为空，眠2
                {
                    die_2++;
                }
            }
        }
        if (Check.checkIncright(chesses, x, y) == 2) {// 右斜线：左上lu+右下rd
            if (lu == 0 && rd == 0) {// 如果两边断开位置都为空
                if ((lu1 == 0 && lu2 == data) || (rd1 == 0 && rd2 == data))// 第三棋子有一个为本色，眠3
                {
                    die_3++;
                } else if (lu1 == 0 && rd1 == 0)// 断开第二个位置都为空，活2
                {
                    // System.out.println("<"+x+","+y+"> 检测到右斜线活二");
                    alive_2++;
                }
                if ((lu1 == data && lu2 == 0) || (rd1 == data && rd2 == 0))
                // 某一边断开第二个位置为本色，第三位置为空，跳活三
                {
                    tiaoalive_3++;
                }
                if ((lu1 == data && lu2 == -data) || (rd1 == data && rd2 == -data))
                // 某一边断开第二位置为本色，得三位置为他色，眠三
                {
                    die_3++;
                }
                if ((lu1 == data && lu2 == data) || (rd1 == data && rd2 == data))
                // 某一边的12都为本色，眠四
                {
                    die_4++;
                }
            } else if (lu == 0 && rd == -data) {// 左边为空右边被拦
                if (lu1 == data && lu2 == data)// 左边12都为本色，眠四
                {
                    die_4++;
                } else if (lu1 == data || lu2 == data)// 左2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (lu1 == 0 && lu2 == 0)// 左123都为空,眠2
                {
                    die_2++;
                }

            } else if (rd == 0 && lu == -data) {// 右边为空左边被拦
                if (rd1 == data && rd2 == data)// 右边12都为本色，眠四
                {
                    die_4++;
                } else if (rd1 == data || rd2 == data)// 右2只要有一个为自己的棋子，眠三
                {
                    die_3++;
                } else if (rd1 == 0 && rd2 == 0)// 右123为空，眠2
                {
                    die_2++;
                }
            }

        }
        */
/**
         * 1连珠
         *//*

        if (Check.checkRow(chesses, x, y) == 1) {// 横向1连
            // 眠四情况01000
            if ((right == 0 && right1 == data && right2 == data && right3 == data)
                    || (left == 0 && left1 == data && left2 == data && left3 == data)) {
                die_4++;
            }
            // 跳三101001
            if ((right == 0 && left == 0 && left1 == data && left2 == data && left3 == 0)
                    || (left == 0 && right == 0 && right1 == data && right2 == data && right3 == 0)) {
                tiaoalive_3++;
            }
            // 眠三
            if ((right == 0 && left == 0 && left1 == data && left2 == data && left3 == -data)
                    || (right == 0 && left == 0 && right1 == data && right2 == data && right3 == -data))// 101002
            {
                die_3++;
            } else if ((right == -data && left == 0 && left1 == data && left2 == data && left3 == 0)
                    || (right == 0 && left == -data && right1 == data && right2 == data && right3 == 0))// 100102
            {
                die_3++;
            } else if ((right == 0 && right1 == 0 && right2 == data && right3 == data)
                    || (left == 0 && left1 == 0 && left2 == data && left3 == data))// 01100
            {
                die_3++;
            } else if ((right == 0 && right1 == data && right2 == 0 && right3 == data)
                    || (left == 0 && left1 == data && left2 == 0 && left3 == data))
                // 活二
                if ((left == 0 && right == 0 && left1 == data && left2 == 0 && left3 == 0)
                        || (right == 0 && left == 0 && right1 == data && right2 == 0 && right3 == 0))
                // 101011
                {
                    lowalive_2++;
                } else if ((left == 0 && right == 0 && right1 == 0 && right2 == data && right3 == 0) || (right == 0 && left == 0 && left1 == 0 && left2 == data && left3 == 0))
                // 101101
                {
                    lowalive_2++;
                }
            // 眠二
            // 低眠2不考虑
        }
        if (Check.checkColumn(chesses, x, y) == 1) {// 纵向1连
            // 眠四情况01000
            if ((up == 0 && up1 == data && up2 == data && up3 == data)
                    || (down == 0 && down1 == data && down2 == data && down3 == data)) {
                die_4++;
            }
            // 跳三101001
            if ((up == 0 && down == 0 && down1 == data && down2 == data && down3 == 0)
                    || (down == 0 && up == 0 && up1 == data && up2 == data && up3 == 0)) {
                tiaoalive_3++;
            }
            // 眠三
            if ((up == 0 && down == 0 && down1 == data && down2 == data && down3 == -data)
                    || (up == 0 && down == 0 && up1 == data && up2 == data && up3 == -data))// 101002
            {
                die_3++;
            } else if ((up == -data && down == 0 && down1 == data && down2 == data && down3 == 0)
                    || (up == 0 && down == -data && up1 == data && up2 == data && up3 == 0))// 100102
            {
                die_3++;
            } else if ((up == 0 && up1 == 0 && up2 == data && up3 == data)
                    || (down == 0 && down1 == 0 && down2 == data && down3 == data))// 01100
            {
                die_3++;
            } else if ((up == 0 && up1 == data && up2 == 0 && up3 == data)
                    || (down == 0 && down1 == data && down2 == 0 && down3 == data)) {
                die_3++;
            }
            // 活二
            if ((down == 0 && up == 0 && down1 == data && down2 == 0 && down3 == 0)
                    || (up == 0 && down == 0 && up1 == data && up2 == 0 && up3 == 0))
            // 101011
            {
                lowalive_2++;
            } else if ((down == 0 && up == 0 && up1 == 0 && up2 == data && up3 == 0)
                    || (up == 0 && down == 0 && down1 == 0 && down2 == data && down3 == 0))
            // 101101
            {
                lowalive_2++;
            }
            // 眠二
            // 低眠2不考虑
        }
        if (Check.checkIncleft(chesses, x, y) == 1) {// 左斜线：左下+右上
            // 眠四情况01000
            if ((ru == 0 && ru1 == data && ru2 == data && ru3 == data)
                    || (ld == 0 && ld1 == data && ld2 == data && ld3 == data)) {
                die_4++;
            }
            // 跳三101001
            if ((ru == 0 && ld == 0 && ld1 == data && ld2 == data && ld3 == 0)
                    || (ld == 0 && ru == 0 && ru1 == data && ru2 == data && ru3 == 0)) {
                tiaoalive_3++;
            }
            // 眠三
            if ((ru == 0 && ld == 0 && ld1 == data && ld2 == data && ld3 == -data)
                    || (ru == 0 && ld == 0 && ru1 == data && ru2 == data && ru3 == -data))// 101002
            {
                die_3++;
            } else if ((ru == -data && ld == 0 && ld1 == data && ld2 == data && ld3 == 0)
                    || (ru == 0 && ld == -data && ru1 == data && ru2 == data && ru3 == 0))// 100102
            {
                die_3++;
            } else if ((ru == 0 && ru1 == 0 && ru2 == data && ru3 == data)
                    || (ld == 0 && ld1 == 0 && ld2 == data && ld3 == data))// 01100
            {
                die_3++;
            } else if ((ru == 0 && ru1 == data && ru2 == 0 && ru3 == data)
                    || (ld == 0 && ld1 == data && ld2 == 0 && ld3 == data)) {
                die_3++;
            }
            // 活二
            if ((ld == 0 && ru == 0 && ld1 == data && ld2 == 0 && ld3 == 0)
                    || (ru == 0 && ld == 0 && ru1 == data && ru2 == 0 && ru3 == 0))
            // 101011
            {
                lowalive_2++;
            } else if ((ld == 0 && ru == 0 && ru1 == 0 && ru2 == data && ru3 == 0)
                    || (ru == 0 && ld == 0 && ld1 == 0 && ld2 == data && ld3 == 0))
            // 101101
            {
                lowalive_2++;
            }
            // 眠二
        }
        if (Check.checkIncright(chesses, x, y) == 1) {// 右斜线：右下rd+左上lu
            // 眠四情况01000
            if ((rd == 0 && rd1 == data && rd2 == data && rd3 == data)
                    || (lu == 0 && lu1 == data && lu2 == data && lu3 == data)) {
                die_4++;
            }
            // 跳三101001
            if ((rd == 0 && lu == 0 && lu1 == data && lu2 == data && lu3 == 0)
                    || (lu == 0 && rd == 0 && rd1 == data && rd2 == data && rd3 == 0)) {
                tiaoalive_3++;
            }
            // 眠三
            if ((rd == 0 && lu == 0 && lu1 == data && lu2 == data && lu3 == -data)
                    || (rd == 0 && lu == 0 && rd1 == data && rd2 == data && rd3 == -data))// 101002
            {
                die_3++;
            } else if ((rd == -data && lu == 0 && lu1 == data && lu2 == data && lu3 == 0)
                    || (rd == 0 && lu == -data && rd1 == data && rd2 == data && rd3 == 0))// 100102
            {
                die_3++;
            } else if ((rd == 0 && rd1 == 0 && rd2 == data && rd3 == data)
                    || (lu == 0 && lu1 == 0 && lu2 == data && lu3 == data))// 01100
            {
                die_3++;
            } else if ((rd == 0 && rd1 == data && rd2 == 0 && rd3 == data)
                    || (lu == 0 && lu1 == data && lu2 == 0 && lu3 == data)) {
                die_3++;
            }
            // 活二
            if ((lu == 0 && rd == 0 && lu1 == data && lu2 == 0 && lu3 == 0)
                    || (rd == 0 && lu == 0 && rd1 == data && rd2 == 0 && rd3 == 0))
            // 101011
            {
                lowalive_2++;
            } else if ((lu == 0 && rd == 0 && rd1 == 0 && rd2 == data && rd3 == 0)
                    || (rd == 0 && lu == 0 && lu1 == 0 && lu2 == data && lu3 == 0))
            // 101101
            {
                lowalive_2++;
            }
            // 眠二
        }
        chesses[x][y] = 0;// 检查假设值之后返回

        return this.getLevel();
    }

    public int getLevel() {
        int count = 1;
        if (five > 0)
            count += 100000;// 赢
        if (alive_4 > 0 || highdie_4 > 1 || (highdie_4 > 0 && alive_3 > 0))// 活4，双死4，死4活3
            count += 10000;
        if (alive_3 > 1)// 双活3
            count += 5000;
        if (die_3 > 0 && alive_3 > 0)// 死3活3
            count += 1000;
        if (highdie_4 > 0)// 死4
            count += 500;
        if (die_4 > 0)// 低级死4
            count += 400;
        if (alive_3 > 0)// 单活3
            count += 100;
        if (tiaoalive_3 > 0)// 跳活3
            count += 90;
        if (alive_2 > 1)// 双活2
            count += 50;
        if (alive_2 > 0)// 活2
            count += 10;
        if (lowalive_2 > 0)// 低活2
            count += 9;
        if (die_3 > 0)// 死3
            count += 5;
        if (die_2 > 0)// 死2
            count += 2;
        // if()
        return count;
    }
}
*/
