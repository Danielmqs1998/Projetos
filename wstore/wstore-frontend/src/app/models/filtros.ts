import { FiltroCategoriaProduto } from "../enums/filtroCategoriaProduto";
import { FiltroCondicaoProduto } from "../enums/filtroCondicaoProduto";
import { FiltroEstadoProduto } from "../enums/filtroEstadoProduto";

export interface Filtros {
  precoMaximo: number | null;
  precoMinimo: number | null;
  descricao: string; 
  categoria: FiltroCategoriaProduto;
  estado: FiltroEstadoProduto;
  condicoes: FiltroCondicaoProduto[];
}