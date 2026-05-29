export class Produto {
    codigo:           string  = '';
    codigoBarra:      string  = '';
    descricao:        string  = '';
    quantidade:       number  = 0;
    preco:            number  = 0;
    precoPromocional: number  = 0;   // adicionado para suportar desconto
    usado:            boolean = false;
    imagem:           string  = '';
    fornecedor:       number  = 0;
    categoria:        number  = 0;
    classificacao:    number  = 0;
    totalAvaliacoes:  number  = 0;
}
