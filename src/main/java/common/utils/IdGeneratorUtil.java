/*
 * MIT License
 *
 * Copyright (c) 2021 yedf
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class IdGeneratorUtil {
    
    private static final int MAX_BRANCH_ID = 99;
    
    private static final int LENGTH = 20;
    
    private String branchId;
    
    private int subBranchId;
    
    public IdGeneratorUtil(String parentId) {
        this.branchId = parentId;
    }
    
    /**
     * 生成全局事务id
     *
     * @param serverUrl
     * @return
     * @throws Exception
     */
    public String genGid(String serverUrl) throws Exception {
        JSONObject jsonObject;
        try {
            String content = HttpUtil.get(serverUrl);
            jsonObject = JSONObject.parseObject(content);
        } catch (Exception e) {
            throw new Exception("Can’t get gid, please check the dtm server.");
        }
        Object code = jsonObject.get("code");
        if (null != code && (int) code > 0) {
            Object message = jsonObject.get("message");
            throw new Exception(message.toString());
        }
        return jsonObject.get("gid").toString();
    }
    
    /**
     * 生成注册分支id
     *
     * @return
     * @throws Exception
     */
    public String genBranchId() throws Exception {
        if (this.subBranchId >= MAX_BRANCH_ID) {
            throw new Exception("branch id is larger than 99");
        }
        if (this.branchId.length() >= LENGTH) {
            throw new Exception("total branch id is longer than 20");
        }
        this.subBranchId++;
        return this.branchId + String.format("%02d", this.subBranchId);
    }
}