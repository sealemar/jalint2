"
" developed by Sergey Markelov (2013)
"

"
" Per language setup
"
function! s:initC()
    setlocal notagrelative
    setlocal tags+=$HOME/.cache/vim-specific/tags/c-home-include
    setlocal tags+=etc/c.ctags
endfunction

function! s:initJava()
    setlocal notagrelative
    setlocal tags+=etc/java.ctags
endfunction

autocmd FileType c call <SID>initC()
autocmd FileType java call <SID>initJava()

"
" Ctags files rebuild
"
autocmd BufWritePost c silent !make ctags-c
autocmd BufWritePost java silent !make ctags-java

"
" Session managemenr
"
function! s:saveSession()
    if filereadable(s:sessionFile)
        execute "silent !mv " . s:sessionFile . " " . s:sessionOldFile
    endif
    execute "mksession " . s:sessionFile
endfunction

function! s:restoreSession()
    if filereadable(s:sessionFile)
        execute "source " .s:sessionFile
    endif
endfunction

set sessionoptions=buffers,curdir,folds,globals,help,localoptions,options,resize,slash,tabpages,unix,winpos,winsize
let s:sessionFile = 'etc/Session.vim'
let s:sessionOldFile = 'etc/Session.old.vim'
autocmd VimLeave * call <SID>saveSession()
autocmd VimEnter * call <SID>restoreSession()

"
" syntastic [javac] support
"

" @return string of format:
"
"         $libDir/first.jar
"         $libDir/second.jar[...]
"
"         or empty string if $libDir is empty
"
function! s:buildClassPath(libDir)
    let result = expand(a:libDir . '/*.jar')

    if ! empty(result)
        let result = "\n" . result
    endif

    return result
endfunction

let g:syntastic_java_javac_classpath = 'build/java' . <SID>buildClassPath('lib')
