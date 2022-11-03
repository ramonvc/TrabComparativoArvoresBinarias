import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.function.Consumer;

public class ArvoreB<T extends Comparable<T>> {
    class Elemento {
        Elemento pai; // Referência para o elemento pai
        Vector<Elemento> filhos; // Vetor de elementos filho
        Vector<T> chaves; // Vetor de chaves (valores)

        public Elemento() {
            filhos = new Vector<>();
            chaves = new Vector<>();
        }
    }

    private Elemento raiz;
    private int ordem;
    private static int cont;

    public ArvoreB(int ordem) {
        this.ordem = ordem;
        raiz = new Elemento();
    }

    public void percorre(Elemento e, Consumer<T> callback) {
        if (e != null) {
            int total = e.chaves.size();

            for (int i = 0; i < total; i++) {
                // Visita o filho da esquerda/direita
                percorre(e.filhos.get(i), callback);

                callback.accept(e.chaves.get(i));
            }

            // Visita ultimo filho (direita)
            percorre(e.filhos.get(total), callback);
        }
    }

    private boolean localizaChave(T chave) {
        Elemento e = raiz;

        while (e != null) {
            int i = pesquisaBinaria(e, chave);

            if (i < e.chaves.size() && e.chaves.get(i).compareTo(chave) == 0) {
                return true; // Encontrou
            } else {
                e = i < e.filhos.size() ? e.filhos.get(i) : null;
            }
        }

        return false; // Não encontrou
    }

    private int pesquisaBinaria(Elemento e, T chave) {
        int inicio = 0, fim = e.chaves.size() - 1, meio;

        while (inicio <= fim) {
            meio = (inicio + fim) / 2;

            if (e.chaves.get(meio).compareTo(chave) == 0) {
                return meio; // Encontrou
            } else if (e.chaves.get(meio).compareTo(chave) > 0) {
                fim = meio - 1;
            } else {
                inicio = meio + 1;
            }
        }

        return inicio; // Não encontrou
    }

    private Elemento localizaNo(T chave) {
        Elemento e = raiz;

        while (e != null) {
            int i = pesquisaBinaria(e, chave);
            Elemento filho = i < e.filhos.size() ? e.filhos.get(i) : null;

            if (filho != null)
                e = filho;
            else
                return e; // Encontrou nó
        }

        return null; // Não encontrou nenhum nó
    }

    private void adicionaChaveNo(Elemento e, Elemento novo, T chave) {
        int i = pesquisaBinaria(e, chave);

        e.chaves.insertElementAt(chave, i);

        if (e.filhos.size() == 0)
            e.filhos.insertElementAt(null, i);

        e.filhos.insertElementAt(novo, i + 1);
    }

    private boolean transbordo(Elemento e) {
        return e.chaves.size() > ordem * 2;
    }

    private Elemento divideNo(Elemento e) {
        int meio = e.chaves.size() / 2;
        Elemento novo = new Elemento();
        novo.pai = e.pai;

        for (int i = meio + 1; i <= e.chaves.size(); i++) {
            cont += 1;
            if (i < e.chaves.size()) {
                T v = e.chaves.get(i);
                novo.chaves.add(v);
            }
            Elemento filho = e.filhos.get(i);
            novo.filhos.add(filho);
            if (filho != null)
                filho.pai = novo;
        }

        e.chaves.subList(meio, e.chaves.size()).clear();
        e.filhos.subList(meio + 1, e.filhos.size()).clear();
        return novo;
    }

    private void adicionaChave(T chave) {
        Elemento e = localizaNo(chave);

        adicionaChaveRecursivo(e, null, chave);
    }

    void adicionaChaveRecursivo(Elemento e, Elemento novo, T chave) {
        adicionaChaveNo(e, novo, chave);

        if (transbordo(e)) {

            T promovido = e.chaves.get(ordem);
            novo = divideNo(e);

            if (e.pai == null) {
                Elemento pai = new Elemento();
                pai.filhos.add(e);
                adicionaChaveNo(pai, novo, promovido);
                e.pai = pai;
                novo.pai = pai;
                raiz = pai;
            } else
                adicionaChaveRecursivo(e.pai, novo, promovido);
        }
    }

    public static void main(String[] args) {
        Set<Integer> set = new HashSet<>();
        while (set.size() < 1000) {
            set.add((int) Math.round(Math.random() * 1000));
        }

        // PIOR CASO
        System.out.println("============ PIOR CASO: ORDEM 01 ============");

        ArvoreB<Integer> aUm = new ArvoreB<>(1);

        for (int i = 0; i < 1000; i++) {
            aUm.adicionaChave(i);
            System.out.print(cont + ",");
        }
        cont = 0;

        System.out.println("============ PIOR CASO: ORDEM 05 ============");

        ArvoreB<Integer> aCinco = new ArvoreB<>(5);

        for (int i = 0; i < 1000; i++) {
            aCinco.adicionaChave(i);
            System.out.print(cont + ",");
        }
        cont = 0;

        System.out.println("============ PIOR CASO: ORDEM 10 ============");

        ArvoreB<Integer> aDez = new ArvoreB<>(10);

        for (int i = 0; i < 1000; i++) {
            aDez.adicionaChave(i);
            System.out.print(cont + ",");
        }
        cont = 0;

        // MÉDIO CASO
        System.out.println("============  MÉDIO CASO: ORDEM 01 ============");

        ArvoreB<Integer> aUmMedio = new ArvoreB<>(1);

        set.forEach(val -> {
            aUmMedio.adicionaChave(val);
            System.out.print(cont + ",");
        });
        cont = 0;

        System.out.println("\n\n============ MÉDIO CASO: ORDEM 05 ============");

        ArvoreB<Integer> aCincoMedio = new ArvoreB<>(5);

        set.forEach(val -> {
            aCincoMedio.adicionaChave(val);
            System.out.print(cont + ",");
        });
        cont = 0;

        System.out.println("\n\n============ MÉDIO CASO: ORDEM 10 ============");

        ArvoreB<Integer> aDezMedio = new ArvoreB<>(10);

        set.forEach(val -> {
            aDezMedio.adicionaChave(val);
            System.out.print(cont + ",");
        });
        cont = 0;

    }
}
