import { Page } from "./rest/page";

export class Produto{
    codigo: string = "";
    codigoBarra: string = "";
    descricao: string = "";
    quantidade: number = 0;
    preco: number = 0;
    imagem: string = "";
    fornecedor: number = 0;
    categoria: number = 0;
    classificacao: number = 0;
    totalAvaliacoes: number = 0;
}