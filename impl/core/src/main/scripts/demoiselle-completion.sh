# bash completion for Demoiselle Tools

have demoiselle && {
_demoiselle()
{
    local cur prev opts base
    COMPREPLY=()
    cur="${COMP_WORDS[COMP_CWORD]}"
    prev="${COMP_WORDS[COMP_CWORD-1]}"

    opts="-g --gui -h --help -i --input -o --output"

    if [[ "$cur" = -* ]]; then
        COMPREPLY=($(compgen -W "${opts}" -- "$cur"))
        return 0
    fi

    case "${prev}" in
        -h|--help|-g|--gui)
            return 0
            ;;
        -i|--input|-o|--output)
            _filedir
            return 0
            ;;
    esac

    DEMOISELLE_TEMPLATES="/opt/demoiselle/tool/nimble/templates/"

    local templates=$(ls -1 $DEMOISELLE_TEMPLATES | grep -v templates.conf)
    COMPREPLY=($(compgen -W "${templates}" -- ${cur}))

    return 0
}

complete -F _demoiselle demoiselle
}

# Local variables:
# mode: shell-script
# sh-basic-offset: 4
# sh-indent-comment: t
# indent-tabs-mode: nil
# End:
# ex: ts=4 sw=4 et filetype=sh