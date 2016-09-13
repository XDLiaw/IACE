$(function(){
    // smooth scroll 
    $('a[href*=#]:not([href=#])').click(function() {
	$("#SUBMENU").hide(100);
      if (location.pathname.replace(/^\//,'') == this.pathname.replace(/^\//,'') && location.hostname == this.hostname) {
        var target = $(this.hash);
        target = target.length ? target : $('[name=' + this.hash.slice(1) +']');
        if (target.length) {
          $('html,body').animate({
            scrollTop: target.offset().top
          }, 1500);
          return false;
        }
      }
    });

    // scroll change menu element
    $(window).scroll(function() {
      var windscroll = $(window).scrollTop();
      $('.menu_focus').each(function(i) {
          if (($(this).position().top-130) <= windscroll) {
              var menuIndex = '#'+$('a:eq(0)', $(this)).attr('id');
              $('#sidebar li a[class*=menu_tag]').removeClass('menu_tag');
              $('a[href^='+menuIndex+']').addClass('menu_tag');
          }
      });
    }).scroll();

    // �� #qaContent �� ul �l�����[�W .accordionPart
    // ���ۦA��X li �����Ĥ@�� div �l�����[�W .qa_title
    // ������[�W hover �� click �ƥ�
    // �P�ɧ�S�̤����[�W .qa_content �����ð_��
    $('#qaContent ul').addClass('accordionPart').find('li div:nth-child(1)').addClass('qa_title').hover(function(){
      $(this).addClass('qa_title_on');
    }, function(){
      $(this).removeClass('qa_title_on');
    }).click(function(){
      // ���I����D�ɡA�Y���׬O���îɫh��ܥ��A�P�����è䥦�w�g�i�}������
      // �Ϥ��h����
	  var $qa_content = $(this).next('div .qa_content');
	  if ($qa_content.css('display') == 'none') {
		$qa_content.slideToggle();
	  }
	  else {
		$qa_content.slideUp();
	  }
	  /*
      var $qa_content = $(this).next('div.qa_content');
      if(!$qa_content.is(':visible')){
        $('#qaContent ul li div.qa_content:visible').slideUp();
      }
      $qa_content.slideToggle();
	  */
    }).siblings().addClass('qa_content').hide();
  });