console.log("Reply Module...");

var replyService = (function () {
  //댓글 데이터저장
  function add(reply, callback, error) {
    console.log("reply....");

    $.ajax({
      type: "post",
      url: "/replies/new",
      data: JSON.stringify(reply),
      contentType: "application/json; charset=utf-8",
      success: function (result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },
      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      },
    });
  }

  //댓글 전체테이타 가져오기
  function getList(param, callback, error) {
    var bno = param.bno;
    var page = param.page || 1;

    $.getJSON("/replies/pages/" + bno +"/" + page + ".json", function(data){
     if(callback) {
       callback(data.replyCnt, data.list); //댓글 숫자와 목록을 가져옴
     }
    }).fail(function(xhr, status, exx){
      if(error){
        error();
      }
    }); 
  
  }
  
  

  //댓글 삭제
  function remove(rno, callback, error) {
    $.ajax({
      type: "delete",
      url: "/replies/" + rno,

      success: function (result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },
      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      },
    });
  }

  //댓글 업데이트
  function update(reply, callback, error) {
    $.ajax({
      type: "put",
      url: "/replies/" + reply.rno,
      data: JSON.stringify(reply),
      contentType: "application/json; charset=urt-8",

      success: function (result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },

      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      },
    });
  }

  //단건 데이터 가져오기
  function get(rno, callback, error) {
    $.ajax({
      type: "get",
      url: "/replies/" + rno + ".json",

      success: function (result, status, xhr) {
        if (callback) {
          callback(result);
        }
      },
      error: function (xhr, status, er) {
        if (error) {
          error(er);
        }
      },
    });
  }

  // 주어진 시간을 현재 시간과 비교하여, 24시간 이내라면 시:분:초로, 그 이상이면 년/월/일로 표시
  function displayTime(timeValue) {
    var today = new Date();

    var gap = today.getTime() - timeValue;

    var dateObj = new Date(timeValue);
    var str = "";

    if (gap < 1000 * 60 * 60 * 24) {
      var hh = dateObj.getHours();
      var mi = dateObj.getMinutes();
      var ss = dateObj.getSeconds();

      return [
        (hh > 9 ? "" : "0") + hh,
        ":",
        (mi > 9 ? "" : "0") + mi,
        ":",
        (ss > 9 ? "" : "0") + ss,
      ].join("");
    } else {
      var yy = dateObj.getFullYear();
      var mm = dateObj.getMonth() + 1; // getMonth() is zero-based
      var dd = dateObj.getDate();

      return [
        yy,
        "/",
        (mm > 9 ? "" : "0") + mm,
        "/",
        (dd > 9 ? "" : "0") + dd,
      ].join("");
    }
  }

  return {
    add: add,
    getList: getList,
    remove: remove,
    update: update,
    get: get,
    displayTime: displayTime,
  };
})();
