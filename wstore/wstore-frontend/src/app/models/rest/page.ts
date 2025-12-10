export interface Page<T> {
  content: T[];             // Lista de elementos da página
  totalElements: number;    // Total de registros
  totalPages: number;       // Total de páginas
  size: number;             // Tamanho da página (quantos por página)
  number: number;           // Página atual (começa em 0)
  first: boolean;           // É a primeira página?
  last: boolean;            // É a última página?
  empty: boolean;           // Está vazia?
}